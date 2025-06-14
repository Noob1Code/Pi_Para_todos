import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class HandleErrorMessageService{

  handleError(error: any): string {
    console.error('Erro recebido no componente (handleError):', error);

    let mensagemErro = 'Ocorreu um erro desconhecido.';

    if (error instanceof Error) {
      mensagemErro = error.message; 
    } 
    else if (error instanceof HttpErrorResponse) {
      const errorResponse = error.error;

      if (error.status === 0) {
        mensagemErro = 'Não foi possível conectar ao servidor. Verifique sua conexão ou tente novamente mais tarde.';
      } else if (errorResponse) {
        if (typeof errorResponse.message === 'string') {
          mensagemErro = errorResponse.message;
        } else if (typeof errorResponse.error === 'string') { 
          mensagemErro = errorResponse.error;
        } else if (typeof errorResponse.erro === 'string') { 
          mensagemErro = errorResponse.erro;
        } else if (errorResponse.messages && typeof errorResponse.messages === 'object') {
          const firstErrorKey = Object.keys(errorResponse.messages)[0];
          if (firstErrorKey) {
            const msg = errorResponse.messages[firstErrorKey];
            mensagemErro = Array.isArray(msg) ? msg[0] : msg;
          } else {
            mensagemErro = 'Erro de validação: Um ou mais campos estão inválidos.';
          }
        } else if (typeof errorResponse === 'string') {
          mensagemErro = errorResponse;
        }
      } else {
        mensagemErro = `Erro ${error.status}: ${error.statusText || 'Um erro inesperado ocorreu.'}`;
      }
    }

    if (mensagemErro === 'Ocorreu um erro desconhecido.' && error?.message) {
      mensagemErro = error.message; 
    }

    return mensagemErro;
  }
}
