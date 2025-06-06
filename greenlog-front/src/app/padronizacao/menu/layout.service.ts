import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class LayoutService {
  private _sidebarOpen = new BehaviorSubject<boolean>(false);
  sidebarOpen$ = this._sidebarOpen.asObservable();

  toggleSidebar() {
    this._sidebarOpen.next(!this._sidebarOpen.value);
  }

  setSidebar(open: boolean) {
    this._sidebarOpen.next(open);
  }
}
