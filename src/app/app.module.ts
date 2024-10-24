import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule} from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { ConversationComponent } from './conversation/conversation.component';
import { CadastroComponent } from './cadastro/cadastro.component';
import { ChatComponent } from './chat/chat.component';
import { ContatoComponent } from './contato/contato.component';

import { AuthService } from './service/auth.service';
import { ChatService } from './service/chat.service';
import { ContatoService } from './service/contato.service';
import { MensagemService } from './service/mensagem.service';
import { UserService } from './service/user.service';



@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    CadastroComponent,
    ChatComponent,
    ConversationComponent,
    ContatoComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
  ],
  providers: [AuthService, ChatService, UserService, MensagemService, ContatoService],
  bootstrap: [AppComponent]
})
export class AppModule { }
