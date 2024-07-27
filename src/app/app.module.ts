import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule} from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { CadastroComponent } from './cadastro/cadastro.component';
import { AuthService } from './auth.service';
import { ChatComponent } from './chat/chat.component';
import { ChatService } from './chat.service';
import { UserService } from './user.service';
import { ConversationComponent } from './conversation/conversation.component';
import { MensagemService } from './mensagem.service';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    CadastroComponent,
    ChatComponent,
    ConversationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
  ],
  providers: [AuthService, ChatService, UserService, MensagemService],
  bootstrap: [AppComponent]
})
export class AppModule { }
