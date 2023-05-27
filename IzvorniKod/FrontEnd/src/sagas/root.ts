import { fork } from "redux-saga/effects";
import { loginFlow, logoutFlow } from "./loginSaga";

export default function* root() {
    yield fork(loginFlow);
    yield fork(logoutFlow);
}