import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from '../../services/authentication.service';
import {User} from '../../models/user';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  role: string;

  constructor(private authenticationService: AuthenticationService) {
  }

  ngOnInit(): void {
    this.role = JSON.parse(localStorage.getItem('authToken')).role;
  }

}
