import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './features/home/home.component';
import { LoginComponent } from './features/auth/components/login/login.component';
import { RegisterComponent } from './features/auth/components/register/register.component';
import { guestOnlyGuard } from './features/auth/guards/guest-only.guard';
import { authRequiredGuard } from './features/auth/guards/auth-required.guard';
import { TopicsPageComponent } from './features/topics/pages/topics-page/topics-page.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'topics', component: TopicsPageComponent, canActivate: [authRequiredGuard] },
  { path: 'login', component: LoginComponent, canActivate: [guestOnlyGuard] },
  { path: 'register', component: RegisterComponent, canActivate: [guestOnlyGuard] },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
