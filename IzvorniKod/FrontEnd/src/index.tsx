import { createRoot } from "react-dom/client";
import "./index.css";
import reportWebVitals from "./reportWebVitals";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { App } from "./App";
import { Provider } from "react-redux";
import { store } from "./store/configureStore";
import { PrivateRoute } from "./containers/Login/PrivateRoute";
import { Login } from "./containers/Login/Login";
import "primeicons/primeicons.css";
import "primereact/resources/primereact.min.css";
import { ToastMessage } from "./containers/ToastMessage/ToastMessage";

const root = createRoot(document.getElementById("root")!);
root.render(
    <Provider store={store}>
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/*" element={<PrivateRoute path="/" component={App} />} />
            </Routes>
        </BrowserRouter>
        <ToastMessage />
    </Provider>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
