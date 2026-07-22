import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { QmslogoService } from './qmslogo.service';
import {
  QMS_LOGO_ACCEPTED_EXTENSIONS,
  QMS_LOGO_FILE_FIELDS,
  QMS_LOGO_INVALID_FORMAT_MESSAGE,
  QMS_LOGO_MAX_FILE_SIZE_BYTES,
  QMS_LOGO_TEXT_FIELDS,
  QMS_LOGO_TOO_LARGE_MESSAGE,
  QmsLogoFileFieldConfig,
  QmsLogoTextFieldConfig,
} from './qmslogo.models';

type FileFieldKey = QmsLogoFileFieldConfig['key'];
type TextFieldKey = QmsLogoTextFieldConfig['key'];

/**
 * QMS Logo (legacy state `Qmslogo`, url `/qmslogo`, self-registered in
 * legacy/HIS-WAR/src/main/webapp/resources/assets/js/controller/master/
 * global/qmslogo.js -- no resolve block, `controllerAs:'vm'` declared but
 * 0 real usage, 100% classic `$scope` pattern).
 *
 * Deliberately NOT built from the standard scaffold's list/search/
 * pagination shape -- this screen has none of that. It's a pure
 * settings/upload form: 8 independent fields (6 file uploads + 2 text
 * inputs), each saved by its own "Save" button, each posting its own
 * `multipart/form-data` request to the SAME single backend endpoint
 * (`POST /api/unit/applicationConfiguration/screen-images`) with a
 * different `imageTypeId` (1-8). Confirmed field-by-field against
 * qmslogo.js: every one of the 8 save functions (`mainScreenBackground`,
 * `afterLoginImg`, `nursingTokenCabinImg`, `kioskHeaderImg`,
 * `kioskFooterImg`, `PoweredloginImg`, `applicationNameFun`,
 * `copyWriteFun`) is independent -- there is no combined/batch submit
 * anywhere in the controller.
 *
 * IMPORTANT, audit-confirmed divergence from the Step-3 task brief:
 * qmslogo.js has ZERO `GET`/load calls anywhere in the file -- it never
 * fetches or previews any EXISTING saved image/value on page load. The
 * `GET .../screen-images/{orgId}/{imageTypeId}` endpoint is real (backend
 * confirmed, see qmslogo.service.ts) and is used by several OTHER,
 * unrelated display controllers (kiosk/nursing/consultant display
 * screens) to render the images this screen saves, but qmslogo.js itself
 * never calls it. This component therefore does NOT auto-load/preview
 * existing images on init -- adding that would be new, unaudited behavior
 * legacy's own admin screen doesn't have, not a faithful port. Only a
 * freshly-selected file gets a client-side FileReader preview, exactly
 * matching the `fileinput`/`filepreview` directive's behavior
 * (fileUploadDirective.js:98-119).
 *
 * Second audit-confirmed quirk, faithfully ported: legacy's view uses a
 * SINGLE shared `$scope.fileError` string for all 6 file inputs (each
 * row's `<p ng-show="fileError">` binds to the exact same scope property,
 * not a per-field one). Picking an invalid file in any one field shows the
 * error message under ALL SIX file rows simultaneously, not just the one
 * that was touched. This is a real, screen-specific bug in legacy's own
 * markup/controller (not a shared-directive-level issue covered by the
 * playbook's standing improvement policy), so it is reproduced as-is via
 * a single `fileError` signal shared by every file field below.
 */
@Component({
  selector: 'app-qmslogo',
  imports: [FormsModule],
  templateUrl: './qmslogo.html',
  styleUrl: './qmslogo.scss',
})
export class Qmslogo {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(QmslogoService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;

  readonly fileFields = QMS_LOGO_FILE_FIELDS;
  readonly textFields = QMS_LOGO_TEXT_FIELDS;
  readonly acceptedExtensionsHint = QMS_LOGO_ACCEPTED_EXTENSIONS.join(', ');

  /** Shared across all 6 file inputs -- see class doc comment above. */
  fileError = signal('');

  private readonly selectedFilesState: Record<FileFieldKey, File | null> = {
    mainScreen: null,
    afterLogin: null,
    nursingTokenCabin: null,
    kioskHeader: null,
    kioskFooter: null,
    loginPageImg: null,
  };
  selectedFiles = signal<Readonly<Record<FileFieldKey, File | null>>>({ ...this.selectedFilesState });

  private readonly previewsState: Record<FileFieldKey, string | null> = {
    mainScreen: null,
    afterLogin: null,
    nursingTokenCabin: null,
    kioskHeader: null,
    kioskFooter: null,
    loginPageImg: null,
  };
  previews = signal<Readonly<Record<FileFieldKey, string | null>>>({ ...this.previewsState });

  private readonly savingState: Record<FileFieldKey | TextFieldKey, boolean> = {
    mainScreen: false,
    afterLogin: false,
    nursingTokenCabin: false,
    kioskHeader: false,
    kioskFooter: false,
    loginPageImg: false,
    applicationName: false,
    copyWrite: false,
  };
  saving = signal<Readonly<Record<FileFieldKey | TextFieldKey, boolean>>>({ ...this.savingState });

  applicationName = signal('');
  copyWrite = signal('');

  /**
   * Mirrors the `validfile` directive's real, effective behavior
   * (extension check first, then the 5MB size check -- see
   * qmslogo.models.ts's QMS_LOGO_MAX_FILE_SIZE_BYTES doc comment for why
   * 5MB, not the nominal-but-dead-in-practice 25MB, is correct here).
   */
  onFileSelected(key: FileFieldKey, event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0] ?? null;

    if (!file) {
      this.setSelectedFile(key, null);
      this.setPreview(key, null);
      return;
    }

    const ext = file.name.split('.').pop()?.toLowerCase() ?? '';
    if (!QMS_LOGO_ACCEPTED_EXTENSIONS.includes(ext as (typeof QMS_LOGO_ACCEPTED_EXTENSIONS)[number])) {
      input.value = '';
      this.setSelectedFile(key, null);
      this.setPreview(key, null);
      this.fileError.set(QMS_LOGO_INVALID_FORMAT_MESSAGE);
      return;
    }

    if (file.size > QMS_LOGO_MAX_FILE_SIZE_BYTES) {
      input.value = '';
      this.setSelectedFile(key, null);
      this.setPreview(key, null);
      this.fileError.set(QMS_LOGO_TOO_LARGE_MESSAGE);
      return;
    }

    this.fileError.set('');
    this.setSelectedFile(key, file);

    const reader = new FileReader();
    reader.onload = () => this.setPreview(key, reader.result as string);
    reader.readAsDataURL(file);
  }

  isSaving(key: FileFieldKey | TextFieldKey): boolean {
    return this.saving()[key];
  }

  /** One independent save action per file field -- no combined/batch submit exists in legacy. */
  saveFileField(config: QmsLogoFileFieldConfig): void {
    const file = this.selectedFiles()[config.key];
    if (!file) {
      this.notify.warning(config.missingFileMessage);
      return;
    }

    this.setSaving(config.key, true);
    this.loader.start();
    this.api
      .saveScreenImageFile({
        imageTypeId: config.imageTypeId,
        organisationId: this.organizationId,
        unitId: this.unitId,
        file,
      })
      .subscribe({
        next: (res) => {
          this.setSaving(config.key, false);
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            // Matches legacy: the 6 file-save functions never reset their
            // own `$scope.<field>` after a successful save -- the selected
            // file/preview intentionally stay as-is.
          } else {
            this.notify.error(res.message ?? 'Save failed');
          }
        },
        error: () => {
          this.setSaving(config.key, false);
          this.loader.stop();
          this.notify.error('Save failed');
        },
      });
  }

  /** One independent save action per text field -- same shared endpoint, `logoName` part instead of `file`. */
  saveTextField(config: QmsLogoTextFieldConfig): void {
    const value = this.textFieldValue(config.key);
    if (!value) {
      this.notify.warning(config.missingValueMessage);
      return;
    }

    this.setSaving(config.key, true);
    this.loader.start();
    this.api
      .saveScreenImageName({
        imageTypeId: config.imageTypeId,
        organisationId: this.organizationId,
        unitId: this.unitId,
        logoName: value,
      })
      .subscribe({
        next: (res) => {
          this.setSaving(config.key, false);
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            // Matches legacy: applicationNameFun/copyWriteFun DO clear
            // their own field to "" after a successful save.
            this.setTextFieldValue(config.key, '');
          } else {
            this.notify.error(res.message ?? 'Save failed');
          }
        },
        error: () => {
          this.setSaving(config.key, false);
          this.loader.stop();
          this.notify.error('Save failed');
        },
      });
  }

  textFieldValue(key: TextFieldKey): string {
    return key === 'applicationName' ? this.applicationName() : this.copyWrite();
  }

  setTextFieldValue(key: TextFieldKey, value: string): void {
    if (key === 'applicationName') {
      this.applicationName.set(value);
    } else {
      this.copyWrite.set(value);
    }
  }

  private setSelectedFile(key: FileFieldKey, file: File | null): void {
    this.selectedFiles.update((state) => ({ ...state, [key]: file }));
  }

  private setPreview(key: FileFieldKey, preview: string | null): void {
    this.previews.update((state) => ({ ...state, [key]: preview }));
  }

  private setSaving(key: FileFieldKey | TextFieldKey, value: boolean): void {
    this.saving.update((state) => ({ ...state, [key]: value }));
  }
}
