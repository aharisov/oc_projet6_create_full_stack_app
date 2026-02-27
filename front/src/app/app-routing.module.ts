import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './features/home/home.component';
import { LoginComponent } from './features/auth/components/login/login.component';
import { RegisterComponent } from './features/auth/components/register/register.component';
import { guestOnlyGuard } from './features/auth/guards/guest-only.guard';
import { authRequiredGuard } from './features/auth/guards/auth-required.guard';
import { TopicsPageComponent } from './features/topics/pages/topics-page/topics-page.component';
import { PostsPageComponent } from './features/posts/pages/posts-page/posts-page.component';
import { PostDetailsPageComponent } from './features/posts/pages/post-details-page/post-details-page.component';
import { PostCreatePageComponent } from './features/posts/pages/post-create-page/post-create-page.component';

const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [guestOnlyGuard] },
  { path: 'posts', component: PostsPageComponent, canActivate: [authRequiredGuard] },
  { path: 'posts/create', component: PostCreatePageComponent, canActivate: [authRequiredGuard] },
  { path: 'posts/:id', component: PostDetailsPageComponent, canActivate: [authRequiredGuard] },
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
