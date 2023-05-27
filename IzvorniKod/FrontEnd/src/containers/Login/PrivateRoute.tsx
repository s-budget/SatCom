import { useDispatch, useSelector } from "react-redux";
import { Navigate, RouteProps, useLocation } from "react-router-dom";
import { SetLoginRedirectPath } from "../../actions/loginRedirectPathActions";
import { AppState } from "../../store/configureStore";

type PrivateRouteProps = RouteProps & {
    component: any;
};

export const PrivateRoute = ({ component: Component, ...rest }: PrivateRouteProps) => {
    const loginState = useSelector((state: AppState) => state.login);
    const token = useSelector((state: AppState) => state.user.token);
    const location = useLocation();
    const dispatch = useDispatch();
    if (loginState.loggedIn && token) return <Component />;
    else {
        const loginRedirectPath = loginState.loggingOut
            ? null
            : `${location.pathname}${location.search}`;
        dispatch(SetLoginRedirectPath(loginRedirectPath));
        return <Navigate to="/login" />;
    }
};
