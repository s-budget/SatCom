import "./userCreationForm.css";
import { useState } from "react";
import { Button } from "primereact/button";
import { Dropdown } from "primereact/dropdown";
import { InputText } from "primereact/inputtext";
import "primereact/resources/themes/lara-light-indigo/theme.css"; //theme
import "primereact/resources/primereact.min.css"; //core css
import "primeicons/primeicons.css"; //icons
import { Password } from "primereact/password";
import { UserRole } from "../../models/userData";
import { createUser } from "../../api/users";
import { useNavigate } from "react-router-dom";
import validator from "validator";
import { useDispatch } from "react-redux";
import { showToastMessage } from "../../actions/toastMessageActions";

export const UserCreationForm = () => {
    const dispatch = useDispatch();
    const [email, setEmail] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [role, setRole] = useState("");
    const navigate = useNavigate();
    const [badRequestMessage, setBadRequestMessage] = useState("");

    const userRoleSelectItems = [
        { label: "Regular User", value: UserRole.User },
        { label: "Super Admin", value: UserRole.SuperAdmin },
        { label: "Satellite Admin", value: UserRole.SatelliteAdmin },
    ];
    const handleCreateUser = async () => {
        if (username.length == 0 || email.length == 0 || password.length == 0 || role.length == 0) {
            setBadRequestMessage("Enter all information before submiting.");
        } else if (!validator.isEmail(email)) {
            setBadRequestMessage("Invalid e-mail address.");
        } else if (password.length < 8) {
            setBadRequestMessage("Password is too short.");
        } else {
            try {
                await createUser(username, email, role, password);
                dispatch(showToastMessage("User created successfully.", "success"));
                navigate("/users");
            } catch (error) {
                dispatch(
                    showToastMessage("An error has occurred while adding a new user.", "error")
                );
                setBadRequestMessage("Invalid input! Try again.");
            }
        }
    };

    return (
        <form>
            <div className="container">
                <div className="component">
                    <h1 className="user-title">CREATE USER</h1>
                </div>

                <div className="component">
                    <span className="p-float-label">
                        <InputText
                            id="in1"
                            value={email}
                            onChange={e => setEmail(e.target.value)}
                        />
                        <label htmlFor="in">Email</label>
                    </span>
                </div>

                <div className="component">
                    {" "}
                    <span className="p-float-label">
                        <InputText
                            id="in2"
                            value={username}
                            onChange={e => setUsername(e.target.value)}
                        />
                        <label htmlFor="in">Username</label>
                    </span>
                </div>

                <div className="component">
                    <span className="p-float-label">
                        <Password
                            value={password}
                            onChange={(e: any) => setPassword(e.target.value)}
                            feedback={false}
                            toggleMask
                        />
                        <label htmlFor="in">Password</label>
                    </span>
                </div>

                <div className="component">
                    <Dropdown
                        value={role}
                        options={userRoleSelectItems}
                        onChange={e => setRole(e.value)}
                        placeholder="Choose Role"
                    />
                </div>

                {badRequestMessage.length > 0 && (
                    <h3 className="error-message">{badRequestMessage}</h3>
                )}
                <div className="component">
                    <Button
                        label="Submit"
                        icon="pi pi-check"
                        iconPos="left"
                        onClick={e => {
                            e.preventDefault();
                            handleCreateUser();
                        }}
                    />
                </div>
            </div>
        </form>
    );
};
export default UserCreationForm;
