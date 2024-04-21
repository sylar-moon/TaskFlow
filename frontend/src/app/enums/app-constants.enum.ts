import {environment} from "../../enviroments/enviroment";


export enum httpOptions {
    
  }

  
export const TOKEN_KEY = 'auth-token';
export const USER_KEY = 'auth-user';


export class AppConstants {
  private static API_BASE_URL = environment.apiBaseUrl;
  private static OAUTH2_URL = AppConstants.API_BASE_URL + "oauth2/authorization/";
  private static REDIRECT_URL = environment.redirectUrl;
  public static LINKEDIN_AUTH_URL = AppConstants.OAUTH2_URL + "google" + AppConstants.REDIRECT_URL;
}