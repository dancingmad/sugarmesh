import { Injectable } from '@angular/core';
import { Noty, NotyType } from './noty';
import { Observable, of, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class NotyService {

  private notyStream:Subject<Noty> = new Subject<Noty>();

  handleError<T> (errorMessage:string, result?: T) {
    // weird construction, the function needs to return a function, which is called when an error occurs
    // actual error object is passed to function below, but we ignore it ()
    return ():Observable<T> => {
      this.notyStream.next(new Noty(errorMessage, NotyType.ERROR));
      return of(result as T);
    };
  }

  addError(errorMessage:string) {
    this.notyStream.next(new Noty(errorMessage, NotyType.ERROR));
  }

  addSuccess(successMessage:string) {
    this.notyStream.next(new Noty(successMessage, NotyType.INFO));
  }

  getNotys():Observable<Noty> {
    return this.notyStream.asObservable();
  }

}
