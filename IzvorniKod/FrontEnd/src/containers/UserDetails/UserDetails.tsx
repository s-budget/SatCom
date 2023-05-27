import jwtDecode from "jwt-decode";
import { Button, ConfirmDialog, Dialog, Password } from "primereact";
import { InputText } from "primereact/inputtext";
import { useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { updateUser } from "../../api/users";
import { AppState } from "../../store/configureStore";
import "./UserDetails.css";
import { logout } from "../../actions/authentificationActions";

export const UserDetails = () => {
    const user = useSelector((state: AppState) => state.user);
    const userData: any = user.token != undefined ? jwtDecode(user.token) : "";
    const [username, setUsername] = useState(user.username!);
    const [password, setPassword] = useState("");
    const [editMode, setEditMode] = useState(false);
    const [logoutModal, setLogoutModal] = useState(false);
    const [badRequestMessage, setBadRequestMessage] = useState("");
    const dispatch = useDispatch();

    const onHide = () => {
        setEditMode(false);
    };

    const renderFooter = () => {
        return (
            <div className="modal-footer">
                <Button
                    label="Cancel"
                    id="cancel"
                    icon="pi pi-times"
                    iconPos="left"
                    onClick={() => {
                        setEditMode(false);
                    }}
                />
                <Button
                    label="Submit"
                    icon="pi pi-check"
                    iconPos="left"
                    onClick={() => {
                        setLogoutModal(true);
                    }}
                />
            </div>
        );
    };

    const handleEditUser = async () => {
        if (username.length == 0 && password.length == 0) {
            setBadRequestMessage("You must fill out at least one field before submiting.");
        } else {
            try {
                const userId: number = user.id!; //inace mi baca error da undefined ne mogu pridruziti stringu ????
                const res = await updateUser(userId, username, password);
                setEditMode(false);
                dispatch(logout());
            } catch (error) {
                setBadRequestMessage("Invalid input! Try again.");
            }
        }
    };

    return (
        <div>
            <h1 className="user-title">USER DETAILS</h1>
            <div className="main-container">
                <div className="details-container">
                    <div className="details-card">
                        <h3>Username: </h3>
                        <p>{userData.sub}</p>
                    </div>
                    <div className="details-card">
                        <h3>Role: </h3>
                        <p>{userData.role}</p>
                    </div>
                    <div className="details-card">
                        <h3>Email: </h3>
                        <p>{user.email}</p>
                    </div>
                </div>
                <div>
                    <Button
                        label="Edit User Details"
                        onClick={() => {
                            setEditMode(true);
                        }}
                    ></Button>
                </div>
            </div>
            <Dialog
                header="Edit User Details"
                visible={editMode}
                onHide={() => onHide()}
                breakpoints={{ "960px": "75vw" }}
                style={{ width: "50vw" }}
                footer={renderFooter()}
            >
                <div className="details-input">
                    <h4>Username: </h4>
                    <InputText value={username} onChange={e => setUsername(e.target.value)} />
                </div>
                <div className="details-input">
                    <h4>Password: </h4>
                    <Password
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                        feedback={false}
                        toggleMask
                    />
                </div>
                {badRequestMessage.length > 0 && (
                    <h3 className="error-message">{badRequestMessage}</h3>
                )}
            </Dialog>

            <ConfirmDialog
                visible={logoutModal}
                onHide={() => setLogoutModal(false)}
                message="Are you sure you want to proceed? You will be logged out."
                header="Edit Confirmation"
                icon="pi pi-exclamation-triangle"
                accept={handleEditUser}
                reject={() => setLogoutModal(false)}
            />
        </div>
    );
};
