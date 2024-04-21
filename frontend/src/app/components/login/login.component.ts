import { CommonModule } from "@angular/common";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from "@angular/forms";
import { Router, ActivatedRoute} from '@angular/router';
import { MatDialogModule } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { TokenStorageService } from '../../sercices/token-storage.service';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    MatFormFieldModule,
    FormsModule,
    MatIconModule,
    ReactiveFormsModule,
    MatInputModule,
    MatDialogModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  constructor(
    private http: HttpClient,    
    private tokenStorage: TokenStorageService,
    private router: Router,
    private route: ActivatedRoute,

    ) {

    

  }

  loginControl = new FormControl("", [Validators.required, Validators.email]);
  emailControl = new FormControl("", [Validators.required, Validators.email]);
  passwordControl = new FormControl("", [Validators.required, Validators.email]);
  isRegister=false;


  ngOnInit(): void {
    const token: string | null = this.route.snapshot.queryParamMap.get("token");

    if(token!=null){
      this.tokenStorage.saveToken(token);
        this.router.navigate(['/']);
    }

  }

   registerUser():void{
    const userData = {
      userName: this.loginControl.value,
      email: this.emailControl.value,
      password: this.passwordControl.value
    };
    console.log(userData);
    


    // if (this.loginControl.valid&&this.emailControl.valid&&this.passwordControl.valid) {
      this.http.post<any>("http://localhost:7000/api/registration",userData).subscribe(

      (response: any) => {
       
        console.log('response after registration');
        console.log(response.token);

        this.tokenStorage.saveToken(response.token);
        this.router.navigate(['/']);

      }

      )

        ;
    }

  //  }



   loginUser():void{
    const userData = {
      email: this.emailControl.value,
      password: this.passwordControl.value
    };

    console.log(userData);
    


    // if (this.emailControl.valid&&this.passwordControl.valid) {
      this.http.post<any>("http://localhost:7000/api/auth",userData).subscribe(

      (response: any) => {
       
        console.log('response after auth');
        console.log(response.token);

        this.tokenStorage.saveToken(response.token);
        this.router.navigate(['/']);

      }

      )

        ;
    }


    authGoogle():void{
      this.router.navigate(['http://localhost:7000/oauth2/authorization/google']);

    }
      
   }

// }
