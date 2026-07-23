/**
 * Parity rebuild of
 * legacy/HIS-WAR/src/main/webapp/resources/assets/js/controller/master/resetPwdconfirmPwdController.js.
 *
 * This screen has no list/detail record of its own -- it's a pure
 * self-service action (3 password fields in, a success/error growl out).
 * `ResetPwdConfirmPwdForm` mirrors the controller's own `$scope.existingPassword`
 * / `$scope.password` / `$scope.confirmPassword` fields exactly; there is no
 * corresponding backend DTO to match since the request body
 * (`{ userId, existingPassword, userPwd }`) is built ad hoc in `resetPwd()`,
 * not from a persisted record shape.
 */
export interface ResetPwdConfirmPwdForm {
  existingPassword: string;
  password: string;
  confirmPassword: string;
}

export function emptyResetPwdConfirmPwdForm(): ResetPwdConfirmPwdForm {
  return {
    existingPassword: '',
    password: '',
    confirmPassword: '',
  };
}

/**
 * Body sent to `POST /adt/resetPassword`, matching the legacy controller's
 * `data` object (ctrl:114-120) verbatim -- `userId` is always the calling
 * session's own id (`$cookies.getObject('cookieObject').userId`), never a
 * user-suppliable value. The backend (fix/resetpassword-identity-binding,
 * not yet merged to main) now rejects any request where `userId` doesn't
 * match the authenticated session's own identity -- this was already the
 * only way legacy's one real caller ever populated this field, so sending
 * the session's own id here needs no change to accommodate that fix.
 */
export interface ResetPasswordRequest {
  userId: number;
  existingPassword: string;
  userPwd: string;
}
