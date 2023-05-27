export const SET_LOGIN_REDIRECT_PATH = "SET_LOGIN_REDIRECT_PATH";
export const CLEAR_LOGIN_REDIRECT_PATH = "CLEAR_LOGIN_REDIRECT_PATH";

export const SetLoginRedirectPath = (payload: string | null) => {
    return { type: SET_LOGIN_REDIRECT_PATH, payload };
};

export const ClearLoginRedirectPath = () => {
    return { type: CLEAR_LOGIN_REDIRECT_PATH };
};
