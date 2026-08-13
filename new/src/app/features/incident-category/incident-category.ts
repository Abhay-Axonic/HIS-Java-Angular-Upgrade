import { Component, inject, signal, OnInit } from '@angular/core';
import { IncidentCategoryService } from './incident-category.service';

@Component({
  selector: 'app-incident-category',
  imports: [],
  templateUrl: './incident-category.html',
  styleUrl: './incident-category.scss',
})
export class IncidentCategoryComponent implements OnInit {
  private readonly service = inject(IncidentCategoryService);

  readonly rows = signal<any[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getCategories(1, 1).subscribe({
      next: (res: any) => {
        const list = res?.listObject;
        this.rows.set(Array.isArray(list) ? list : []);
        this.loading.set(false);
      },
      error: (err: any) => {
        this.loading.set(false);
        this.error.set('Category list failed: ' + (err?.error?.message ?? 'the server rejected the request'));
      },
    });
  }
}
