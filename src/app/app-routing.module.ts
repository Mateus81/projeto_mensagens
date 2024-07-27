import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { CadastroComponent } from './cadastro/cadastro.component';
import { ChatComponent } from './chat/chat.component';
import { ConversationComponent } from './conversation/conversation.component';

const routes: Routes = [{path: 'home', component: HomeComponent},
{path: 'login', component: LoginComponent}, {path: 'cadastro', component: CadastroComponent}, 
{path: 'chat', component: ChatComponent}, {path: 'conversa/:id', component: ConversationComponent},
{path: '', redirectTo: '/home', pathMatch: 'full'}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
