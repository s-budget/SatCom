import { Reducer } from "redux";
import {
    IToastSeverity,
    SHOW_TOAST_MESSAGE
} from "../actions/toastMessageActions";

export interface ToastMessageState {
    text: string;
    severity: IToastSeverity;
    life: number;
    timestamp: number;
}

export const initialState: ToastMessageState = {
    text: "",
    severity: "success",
    life: 3000,
    timestamp: 0,
};

export const toastMessageReducer: Reducer<ToastMessageState> = (
    state: ToastMessageState = initialState,
    action: any
): ToastMessageState => {
    switch (action.type) {
        case SHOW_TOAST_MESSAGE:
            return {
                text: action.text,
                severity: action.severity,
                life: action.life,
                timestamp: Date.now(),
            };
        default:
            return state;
    }
};
