import { Component, OnInit } from '@angular/core';
import {UserService} from '../../services/user.service';
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';
import {NotyService} from '../../services/noty.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private userService: UserService) {
  }

  ngOnInit() {
  }

  authenticate(username: string, password: string) {
     // will propagate current user after authentication to all components
     this.userService.authenticate(username, password);
  }

  register(username: string, password: string) {
    // will propagate current user after register to all components
    this.userService.register(username, password);
  }

}
