import { Reducer } from "redux";
import { CLEAR_LOGIN_REDIRECT_PATH, SET_LOGIN_REDIRECT_PATH } from "../actions/loginRedirectPathActions";
 
export const initialState: string | null = null;

export const loginRedirectPathReducer: Reducer<string | null> = (
    state: string | null = initialState,
    action: any
): string | null => {
    switch (action.type) {
        case SET_LOGIN_REDIRECT_PATH:
            return action.payload;
        case CLEAR_LOGIN_REDIRECT_PATH:
            return null;
        default:
            return state;
    }
};

export default loginRedirectPathReducer;
