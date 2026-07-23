import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { ResetPwdConfirmPwdService } from './reset-pwd-confirm-pwd.service';
import { ResetPwdConfirmPwdForm, emptyResetPwdConfirmPwdForm } from './reset-pwd-confirm-pwd.models';

/**
 * Parity rebuild of
 * legacy/HIS-WAR/src/main/webapp/views/reset-password-confirm-pwd.html +
 * resources/assets/js/controller/master/resetPwdconfirmPwdController.js
 * (`resetPwdconfirmPwdController`, no `controllerAs`, plain `$scope`).
 * State `resetPwdconfirmPwd` self-registered at the bottom of the
 * controller's own file (ctrl:147-154, `url: '/resetPwdconfirmPwd'`) --
 * NO `resolve` block at all, so this route has no `data.moduleId`/
 * `requireSuperUser`, same shape as `billingGroupMaster`/`periodicityService`.
 *
 * This is a pure self-service "change my own password" action -- no list,
 * no search, no status toggle. `$scope.init()` (ctrl:8-23) redirects to
 * login when `cookieObject` is missing; `moduleAccessGuard` (wired at the
 * route level, no `moduleId` needed) already enforces the equivalent
 * "must be logged in" baseline check before this component ever renders,
 * so there is no separate defensive check to add here (same pattern as
 * `billingGroupMaster`/`pesapalProcessPage`).
 *
 * **All 3 validation layers ported faithfully, in legacy's exact order**
 * (ctrl:45-108, `resetPwd()`):
 *  1. HTML5 `required` on all 3 fields (template only -- see .html).
 *  2. Empty-field checks, in field order: existingPassword, then password,
 *     then confirmPassword (ctrl:49-76).
 *  3. existingPassword === password -> reject as "must be different"
 *     (ctrl:78-86) -- checked BEFORE the confirm-match check.
 *  4. confirmPassword !== password -> reject as "must match" (ctrl:88-96).
 *  5. `validatePassword()` complexity check -- 8-16 chars, upper+lower+
 *     digit+special (ctrl:31-41, ctrl:98-108) -- checked LAST, only once
 *     all the above already passed.
 *
 * `userId` in the request body is always the calling session's own id
 * (`$cookies.getObject('cookieObject').userId`, ctrl:114-120) -- this
 * screen is exclusively self-service, confirmed via exhaustive frontend
 * audit; there is no "reset another user's password" capability anywhere
 * in this view/controller, and none is added here. The backend
 * (`fix/resetpassword-identity-binding`, not yet merged to `main`) now
 * rejects any request where `userId` doesn't match the authenticated
 * session's own identity -- sending the session's own id, as legacy always
 * did, already satisfies that; no frontend change needed for the fix.
 *
 * Confirm-password matching is client-only, exactly like legacy -- never
 * sent to the backend (only `existingPassword`/`userPwd` are).
 */
@Component({
  selector: 'app-reset-pwd-confirm-pwd',
  imports: [FormsModule],
  templateUrl: './reset-pwd-confirm-pwd.html',
  styleUrl: './reset-pwd-confirm-pwd.scss',
})
export class ResetPwdConfirmPwd {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(ResetPwdConfirmPwdService);

  // ---- form state (mirrors $scope.existingPassword / $scope.password /
  // $scope.confirmPassword, ctrl:27-29) ----
  form = signal<ResetPwdConfirmPwdForm>(emptyResetPwdConfirmPwdForm());

  // ---- "Show Password" checkbox (mirrors the view's onclick="myFunction()"
  // toggle, view:66/110-131) -- toggles all 3 fields' visibility together,
  // same as legacy. ----
  showPassword = signal(false);

  updateField<K extends keyof ResetPwdConfirmPwdForm>(field: K, value: ResetPwdConfirmPwdForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  toggleShowPassword(): void {
    this.showPassword.update((v) => !v);
  }

  // ---- complexity check (mirrors validatePassword, ctrl:31-41) ----
  private validatePassword(password: string): boolean {
    const minLength = 8;
    const maxLength = 16;
    const hasUppercase = /[A-Z]/.test(password);
    const hasLowercase = /[a-z]/.test(password);
    const hasDigit = /[0-9]/.test(password);
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);
    const isLengthValid = password.length >= minLength && password.length <= maxLength;
    return hasUppercase && hasLowercase && hasDigit && hasSpecialChar && isLengthValid;
  }

  // ---- submit (mirrors resetPwd(), ctrl:45-144) ----
  resetPwd(): void {
    const f = this.form();

    if (f.existingPassword === undefined || f.existingPassword === null || f.existingPassword === '') {
      this.notify.warning('Please Enter Existing Password  .');
      return;
    }
    if (f.password === undefined || f.password === null || f.password === '') {
      this.notify.warning('Please Enter New Password  .');
      return;
    }
    if (f.confirmPassword === undefined || f.confirmPassword === null || f.confirmPassword === '') {
      this.notify.warning('Please Enter Confirm Password  .');
      return;
    }
    if (f.existingPassword === f.password) {
      this.notify.warning('Existing Password and new password should be different .');
      return;
    }
    if (f.confirmPassword !== f.password) {
      this.notify.warning('Confirm password and new password must be match .');
      return;
    }

    const isPasswordValid = this.validatePassword(f.password);
    if (!isPasswordValid) {
      this.notify.warning(
        'Password must be 8 to 16 characters long and include uppercase letters, lowercase letters, digits, and special characters.',
      );
      return;
    }

    this.loader.start();
    const userId = this.session.userId ?? 0;
    this.api
      .resetPassword({
        userId,
        existingPassword: f.existingPassword,
        userPwd: f.password,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Password Updated Successfully.');
            this.form.set(emptyResetPwdConfirmPwdForm());
          } else if (res.status === 'error') {
            // ctrl:136-140 -- legacy growls this on `.warning`, not `.error`.
            this.notify.warning(res.message ?? 'Password update failed.');
          }
        },
        error: () => {
          this.loader.stop();
          this.notify.warning('Password update failed.');
        },
      });
  }
}
