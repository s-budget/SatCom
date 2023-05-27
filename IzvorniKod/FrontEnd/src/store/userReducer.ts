import { Reducer } from "redux";
import { AuthentificationActionType, CLEAR_PASSWORD_RECOVERY_TOKEN, SET_USER, SET_USER_LOCALE } from "../actions/authentificationActions";
import { Locale, UserData } from "../models/userData";

const getSavedState = () => {
    const stateStr = sessionStorage.getItem("user");
    if (stateStr) {
        const state : UserData = JSON.parse(stateStr);
        return state;
    } else {
        return null;
    }
}

const savedState: UserData | null = getSavedState();

export const initialState: UserData = {
    username: undefined,
    token: undefined,
    locale: Locale.en,
};

export const userReducer: Reducer<UserData> = (
    state: UserData = savedState || initialState,
    action: AuthentificationActionType
): UserData => {
    switch (action.type) {
        case SET_USER:
            return { ...action.user };
        case CLEAR_PASSWORD_RECOVERY_TOKEN:
            return { ...state, passwordRecoveryToken: undefined };
        case SET_USER_LOCALE:
            return { ...state, locale: action.locale };
        default:
            return state;
    }
};

export default userReducer;