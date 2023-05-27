import { UserData, Locale } from "../models/userData";

export const SET_AUTH = "SET_AUTH";
export const SENDING_LOGIN_REQUEST = "SENDING_LOGIN_REQUEST";
export const LOGIN_REQUEST = "LOGIN_REQUEST";
export const REGISTER_REQUEST = "REGISTER_REQUEST";
export const LOGOUT = "LOGOUT";
export const REQUEST_LOGIN_ERROR = "REQUEST_LOGIN_ERROR";
export const CLEAR_LOGIN_ERROR = "CLEAR_LOGIN_ERROR";
export const SET_USER = "SET_USER";
export const SET_SUBSCRIBING = "SET_SUBSCRIBING";
export const CLEAR_SUBSCRIBING = "CLEAR_SUBSCRIBING";
export const CLEAR_PASSWORD_RECOVERY_TOKEN = "CLEAR_PASSWORD_RECOVERY_TOKEN";
export const SET_USER_LOCALE = "SET_USER_LOCALE";
export const CLEAR_REDUX = "CLEAR_REDUX";

export interface LoginRequestData {
    username: string;
    password: string;
}

export interface SetAuthAction {
    type: typeof SET_AUTH;
    newAuthState: boolean;
    loggingOut?: boolean;
    expired?: boolean;
}
export interface SendingLoginRequestAction {
    type: typeof SENDING_LOGIN_REQUEST;
    sending: boolean;
}
export interface LoginRequestAction {
    type: typeof LOGIN_REQUEST;
    data: LoginRequestData;
}
export interface LogoutAction {
    type: typeof LOGOUT;
}
export interface RequestLoginErrorAction {
    type: typeof REQUEST_LOGIN_ERROR;
    error: string;
}
export interface ClearLoginErrorAction {
    type: typeof CLEAR_LOGIN_ERROR;
}
export interface SetUserAction {
    type: typeof SET_USER;
    user: UserData;
}
export interface SetSubscribingAction {
    type: typeof SET_SUBSCRIBING;
}
export interface ClearSubscribingAction {
    type: typeof CLEAR_SUBSCRIBING;
}

export interface ClearPasswordRecoveryToken {
    type: typeof CLEAR_PASSWORD_RECOVERY_TOKEN;
}

export interface SetUserLocale {
    locale: Locale;
    type: typeof SET_USER_LOCALE;
}

export interface ClearRedux {
    type: typeof CLEAR_REDUX;
}

export type AuthentificationActionType =
    | SetAuthAction
    | SendingLoginRequestAction
    | LoginRequestAction
    | LogoutAction
    | RequestLoginErrorAction
    | ClearLoginErrorAction
    | SetUserAction
    | SetSubscribingAction
    | ClearSubscribingAction
    | ClearPasswordRecoveryToken
    | SetUserLocale
    | ClearRedux;

/**
 * Sets the authentication state of the application
 * @param  {boolean} newAuthState True means a user is logged in, false means no user is logged in
 */
export function setAuthState(
    newAuthState: boolean,
    loggingOut?: boolean,
    expired?: boolean
): AuthentificationActionType {
    return { type: SET_AUTH, newAuthState, loggingOut, expired };
}

/**
 * Sets the `currentlySending` state, which displays a loading indicator during requests
 * @param  {boolean} sending True means we're sending a request, false means we're not
 */
export function sendingLoginRequest(sending: boolean): AuthentificationActionType {
    return { type: SENDING_LOGIN_REQUEST, sending };
}

/**
 * Tells the app we want to log in a user
 * @param  {object} data          The data we're sending for log in
 * @param  {string} data.username The username of the user to log in
 * @param  {string} data.password The password of the user to log in
 */
export function loginRequest(data: LoginRequestData): AuthentificationActionType {
    return { type: LOGIN_REQUEST, data };
}

/**
 * Tells the app we want to log out a user
 */
export function logout() {
    return { type: LOGOUT };
}

/**
 * Sets the `error` state to the error received
 * @param  {object} error The error we got when trying to make the request
 */
export function requestError(error: string): AuthentificationActionType {
    return { type: REQUEST_LOGIN_ERROR, error };
}

/**
 * Sets the `error` state as empty
 */
export function clearLoginError(): AuthentificationActionType {
    return { type: CLEAR_LOGIN_ERROR };
}

/**
 * Sets the `user` state
 */
export function setUser(user: UserData): AuthentificationActionType {
    return { type: SET_USER, user };
}

export function setSubscribing(): AuthentificationActionType {
    return { type: SET_SUBSCRIBING };
}

export function clearSubscribing(): AuthentificationActionType {
    return { type: CLEAR_SUBSCRIBING };
}

export function clearPasswordRecoveryToken(): AuthentificationActionType {
    return { type: CLEAR_PASSWORD_RECOVERY_TOKEN };
}

export function setUserLocale(locale: Locale): AuthentificationActionType {
    return { type: SET_USER_LOCALE, locale };
}

 export function clearRedux() : AuthentificationActionType {
    return { type: CLEAR_REDUX };
}
