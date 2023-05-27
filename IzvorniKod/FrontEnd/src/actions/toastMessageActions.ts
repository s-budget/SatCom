export const SHOW_TOAST_MESSAGE = "SHOW_TOAST_MESSAGE";

export type IToastSeverity = "success" | "info" | "warn" | "error";

export const showToastMessage = (text: string, severity: IToastSeverity = "info", life = 3000) => {
    return { type: SHOW_TOAST_MESSAGE, text, severity, life };
};
