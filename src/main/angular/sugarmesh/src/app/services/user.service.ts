import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of, Subject } from 'rxjs';
import { User } from './user';
import { NotyService } from './noty.service';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  private userUrl = 'api/user/';
  private pendingUser = new Subject<User>();
  private fetchedUser: User;

  constructor(private http: HttpClient,
              private notyService: NotyService) {
    const url = `${this.userUrl}`;
    this.http.get<User>(url + 'current').subscribe(
      user => {
        this.pendingUser.next(user);
      },
    );
    this.pendingUser.asObservable().subscribe(user => this.fetchedUser = user);
  }

  getLoggedInUser(): Observable<User> {
    if (this.fetchedUser) {
      return of(this.fetchedUser);
    }
    return this.pendingUser.asObservable();
  }

  authenticate(username: string, password: string) {
    const url = `${this.userUrl}${username}/authentication?password=${password}`;
    this.http.get<User>(url).subscribe(
      user => this.pendingUser.next(user),
      error => {
        if (error.status === 401) {
          this.notyService.addError('Could not login, incorrect username/password');
        } else if (error.status === 404) {
          this.notyService.addError('Server is down, please try again later');
        } else {
          this.notyService.addError('Authentication failed:' + error.statusText);
        }
      });
  }

  register(username: string, password: string) {
    const url = `${this.userUrl}`;
    this.http.post<User>(url, { username, password }, this.httpOptions).subscribe(
      user => this.pendingUser.next(user),
      error => {
        if (error.status === 409) {
          this.notyService.addError('User ' + username + ' is already registered, please use a different username');
        } else if (error.status === 404) {
          this.notyService.addError('Server is down, please try again later');
        }
      },
    );
  }

  update(user:User) {
    this.http.put<User>(this.userUrl + user.id, user).subscribe(
      () => this.notyService.addSuccess('Updated user profile'),
      error => this.notyService.addError('Could not update user profile'),
    );
  }

  list():Observable<User[]> {
    return this.http.get<User[]>(this.userUrl);
  }

  logout() {
    this.http.get<User>(this.userUrl + 'logout').subscribe(
      user => this.pendingUser.next(null),
      error => this.pendingUser.next(null),
    );
  }

}
