import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-btn-return',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './btn-return.component.html',
  styleUrl: './btn-return.component.css',
})
export class BtnReturn {
  @Input() link: string[] = ['/'];
}
