import { applyMiddleware, combineReducers, compose, createStore, Reducer } from "redux";
import createSagaMiddleware from "redux-saga";
import root from "../sagas/root";
import { UserData } from "../models/userData";
import { loginReducer as login, LoginState } from "./loginReducer";
import {userReducer as user} from "./userReducer";
import { CLEAR_REDUX } from "../actions/authentificationActions";
import { getClearState } from "../helpers/ReduxHelper";
import { loginRedirectPathReducer as loginRedirectPath } from "./loginRedirectPathReducer";
import { toastMessageReducer as toastMessage, ToastMessageState } from "./toastMessageReducer";

export interface AppState {
    login: LoginState;
    user: UserData;
    loginRedirectPath: string | null;
    toastMessage: ToastMessageState;

}

const configureStore = (initialState?: AppState) => {
    const clearState = getClearState();
    const appReducer = combineReducers<AppState>({
        login,
        user,
        loginRedirectPath,
        toastMessage
    });
    
    const rootReducer: Reducer<AppState> = (state, action) => {
        if (action.type === CLEAR_REDUX && state) {
            return appReducer({ login: state.login, ...clearState }, action);
        }
        return appReducer(state, action);
    };

    const enhancers = [];
    const windowIfDefined = typeof window === "undefined" ? null : (window as any);
    if (windowIfDefined && windowIfDefined.__REDUX_DEVTOOLS_EXTENSION__) {
        enhancers.push(windowIfDefined.__REDUX_DEVTOOLS_EXTENSION__());
    }
    
    const sagaMiddleware = createSagaMiddleware();
    const result = createStore(
        rootReducer,
        initialState,
        compose(applyMiddleware(sagaMiddleware), ...enhancers)
    );

    sagaMiddleware.run(root);

    return result;
}

export const store = configureStore();

export type AppDispatch = typeof store.dispatch;
