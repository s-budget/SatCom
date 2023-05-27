import { useSelector } from "react-redux";
import { Navigate } from "react-router-dom";
import { AppState } from "../../store/configureStore";

export const RedirectIfLoggedIn = () => {
    const user = useSelector((state: AppState) => state.user);
    const login = useSelector((state: AppState) => state.login);
    const loginRedirectPath = useSelector((state: AppState) => state.loginRedirectPath);
    if (user.token && login.loggedIn) {
        const path = loginRedirectPath ?? "/";
        return <Navigate to={path} />;
    } else return null;
};
