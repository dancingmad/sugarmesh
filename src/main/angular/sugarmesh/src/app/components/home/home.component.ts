import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../services/user';
import { GraphService } from '../../services/graph.service';
import {Graph} from '../../services/graph';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {

  user: User;
  graph = new Graph('com.irongrp', 'http://localhost:8080/actuator/beans');

  constructor(private userService:UserService,
              private graphService:GraphService) { }

  ngOnInit() {
    this.userService.getLoggedInUser().subscribe(
      (user) => {
        this.user = user;
      },
    );
  }

  createGraph() {
    this.graphService.addGraph(this.graph).subscribe();
  }

}
