import { Component, OnInit } from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../services/user';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  user: User;
  languages: string[];

  constructor(private userService:UserService,
              private translationService:TranslateService) {
  }

  ngOnInit() {
    this.userService.getLoggedInUser().subscribe(u => this.user = u);
    this.languages = ['ENGLISH','GERMAN'];
  }

  onChangeLanguage() {
    this.translationService.use(this.user.language);
    this.userService.update(this.user);
  }

}
