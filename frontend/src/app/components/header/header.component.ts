import { Component, type OnInit } from '@angular/core';
import { UserService } from '../../sercices/user.service';
import { User } from '../../models/user.model';
@Component({
  selector: 'app-header',
  standalone: true,
  imports: [],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  userName = "";
  userPic = "assets/userpic.png";
  userEmail = "";
  roles: String[] = [];
  ngOnInit(): void {
    console.log("header init");

    this.initUserInfo();
  }

  constructor(private userService: UserService) { }

  initUserInfo(): void {

    this.userService.getMe().subscribe(response => {

      this.userName = response.name;
      this.userEmail = response.email;

      if(response.roles!=null){
        this.roles=response.roles
      }

      if (response.profilePictureUrl != null) {
        this.userPic = response.profilePictureUrl;

      }

    })
  }


}
