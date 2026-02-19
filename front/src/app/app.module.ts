import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { HomeComponent } from './features/home/home.component';
import { LoginComponent } from './features/auth/components/login/login.component';
import { RegisterComponent } from './features/auth/components/register/register.component';
import { TopicComponent } from './topic/topic.component';
import { BtnReturn } from "src/app/components/btn-return/btn-return";

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    TopicComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    AppRoutingModule,
    RegisterComponent,
    BtnReturn
],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
