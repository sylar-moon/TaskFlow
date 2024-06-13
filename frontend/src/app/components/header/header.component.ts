import { Component, type OnInit } from '@angular/core';
import { UserService } from '../../sercices/user.service';
import { User } from '../../models/user.model';
import { Router, ActivatedRoute} from '@angular/router';

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

  constructor(private userService: UserService,   
     private router: Router
  ) { }

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

  openClosedTasks():void{
    this.router.navigate(['/closed-tasks']);

  }

  openHomePage():void{
    this.router.navigate(['/']);

  }

  openAllTasks():void{
    this.router.navigate(['/all-tasks']);

  }
}
