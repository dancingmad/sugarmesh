import { Component, OnInit } from '@angular/core';
import {NotyService} from '../../services/noty.service';
import {Noty, NotyType} from '../../services/noty';
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-noty',
  templateUrl: './noty.component.html',
  styleUrls: ['./noty.component.css'],
  animations: [
    trigger('notyTrigger', [
      transition('* => void', [
        style({ opacity: 1 }),
        animate("0.5s ease", style({ opacity:0 }))
      ]),
      transition('void => *', [
        style({ opacity: 0 }),
        animate("0.5s ease", style({ opacity: 1 }))
      ])
    ])
  ],
})
export class NotyComponent implements OnInit {

  activeNotys:Noty[] = [];

  constructor(public notyService:NotyService) { }

  ngOnInit() {
    this.notyService.getNotys().subscribe(noty => {
      this.activeNotys.push(noty);
      setTimeout(() => this.activeNotys.splice(0,1),3000);
    });
  }

}
