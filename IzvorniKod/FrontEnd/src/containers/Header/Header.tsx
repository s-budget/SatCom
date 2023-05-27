import { Link, useNavigate } from "react-router-dom";
import "./Header.css";
import { getHeaderItems, ISimpleHeaderItems } from "./HeaderNavigationItems";
import "@fortawesome/fontawesome-free/css/all.min.css";
import { Button } from "primereact/button";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "../../actions/authentificationActions";
import { AppState } from "../../store/configureStore";
import { userHasRole, UserRole } from "../../models/userData";
import "primeicons/primeicons.css";
import { useEffect, useRef, useState } from "react";

export const Header = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const user = useSelector((state: AppState) => state.user) as any;
    const headerItems: ISimpleHeaderItems[] = getHeaderItems(user.role) ?? [];
    const [isUserMenuShown, setIsUserMenuShown] = useState(false);
    const ref = useRef(null);

    useEffect(() => {
        const closeUserMenu = (event: any) => {
            if (event.path[0].tagName != "I") {
                setIsUserMenuShown(false);
            }
        };
        document.body.addEventListener("click", closeUserMenu);

        return () => document.body.removeEventListener("click", closeUserMenu);
    }, []);

    const start = (
        <Link to="/" aria-label="Početak headera">
            <div>
                <img alt="logo" src="./logo.png" height={"50px"}></img>
            </div>
        </Link>
    );

    const userMenu = (
        <div className="user-menu">
            <div className="user-option" onClick={() => navigate("/user-details")}>
                <span>
                    <i className="pi pi-info-circle"></i>User Details
                </span>
            </div>
            <div className="user-option" onClick={() => navigate("/message-history")}>
                <span>
                    <i className="pi pi-history"></i>Message History
                </span>
            </div>
            <div className="user-option" onClick={() => dispatch(logout())}>
                <span>
                    <i className="pi pi-sign-out"></i>Log Out
                </span>
            </div>
        </div>
    );

    const end = (
        <div className="header-end-buttons">
            {/* <Button label="Log Out" onClick={() => dispatch(logout())} /> */}
            <i onClick={() => setIsUserMenuShown(!isUserMenuShown)} className="pi pi-user user"></i>
            {isUserMenuShown && userMenu}
        </div>
    );

    return (
        <header>
            <div className="menubar">
                {start}
                <nav className="header-content">
                    {headerItems.map(item => {
                        return (
                            <div key={item.link}>
                                <Link className="menu-item" to={item.link} aria-label={item.label}>
                                    <span>{item.label}</span>
                                </Link>
                            </div>
                        );
                    })}
                </nav>
                {end}
            </div>
        </header>
    );
};
