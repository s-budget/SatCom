import { Column } from "primereact/column";
import { DataTable } from "primereact/datatable";
import { useCallback, useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { Field, Form } from "react-final-form";
import { Button } from "primereact/button";
import { AppState } from "../../store/configureStore";
import { userHasRole, UserRole } from "../../models/userData";
import { Dialog } from "primereact/dialog";
import { Dropdown, InputText } from "primereact";
import { ILink } from "../../models/link";
import { deleteLink, editLink, getLinkById } from "../../api/links";
import "./LinkDetails.css";
import { showToastMessage } from "../../actions/toastMessageActions";
import { getAllModes } from "../../api/mode";

interface ILocationState {
    link: ILink;
}

export const LinkDetails = () => {
    const dispatch = useDispatch();
    const location = useLocation();
    const navigate = useNavigate();
    const user = useSelector((state: AppState) => state.user);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [modeOptions, setModeOptions] = useState([]);
    const [sendedLink, setSendedLink] = useState((location.state as ILocationState)?.link);

    const onSubmit = async (data: ILink) => {
        if (!data) return;
        try {
            await editLink(data.linkId ?? -1, data);
            dispatch(showToastMessage("Link successfully edited.", "success"));
            const res = await getLinkById(sendedLink.linkId ?? -1);
            setSendedLink(res);
        } catch (error) {
            dispatch(showToastMessage("An error has occurred while editing link.", "error"));
        } finally {
            setDialogOpen(false);
        }
    };

  
    const fetchModes = useCallback(async () => {
        try {
            const res = await getAllModes();
            setModeOptions(res);
        } catch (error) {
            dispatch(
                showToastMessage("An error has occurred while fetching all modes.", "error")
            );
        }
    }, [dispatch]);


    useEffect(() => {
        if (!location.state) {
            navigate("/");
        }
        fetchModes();
    }, [location.state, navigate, fetchModes]);

    const handleDeleteLink = async (id: number) => {
        try {
            await deleteLink(id);
            dispatch(showToastMessage("Link successfully deleted.", "success"));
        } catch (error) {
            dispatch(showToastMessage("An error has occurred while deleting link.", "error"));
        } finally {
            navigate("/links");
        }
    };

    return (
        <div className="link-container">
            <div className="link-options">
                <i
                    className={"fa fa-backward show-cursor back-action"}
                    onClick={() => navigate("/links")}
                >
                    {"   "}B a c k
                </i>
                {userHasRole(user, UserRole.SatelliteAdmin) && (
                    <div className="options">
                        <Button
                            label="Delete"
                            className="delete-button"
                            onClick={() => {
                                handleDeleteLink(sendedLink.linkId ?? -1);
                            }}
                        />
                        <Button
                            label="Edit"
                            className="edit-button"
                            onClick={() => {
                                setDialogOpen(true);
                            }}
                        />
                    </div>
                )}
            </div>
            <h3>Link mode: {sendedLink.linkMode}</h3>
            <h3>Link baud: {sendedLink.linkBaud}</h3>
            <h3>Link frequency: {sendedLink.linkFreq}</h3>
            <hr />
            <Dialog
                visible={dialogOpen}
                id="edit-link"
                header={"Edit link details"}
                draggable={false}
                onHide={() => setDialogOpen(false)}
            >
                <Form
                    onSubmit={(data: ILink) => onSubmit(data)}
                    initialValues={sendedLink}
                    render={({ handleSubmit }) => (
                        <form
                            id="edit-link"
                            onSubmit={handleSubmit}
                            className="form-container"
                            autoComplete="off"
                        >
                            <Field
                                name="linkMode"
                                render={({ input }) => (
                                    <div className="field">
                                        <span>Link Mode</span>
                                        <span className="p-float-label">
                                            <Dropdown
                                                id="linkMode"
                                                {...input}
                                                options={modeOptions}
                                                virtualScrollerOptions={{ itemSize: 38 }}
                                            />
                                        </span>
                                    </div>
                                )}
                            />
                            <Field
                                name="linkFreq"
                                render={({ input }) => (
                                    <div className="field">
                                        <span>Link frequency</span>
                                        <span className="p-float-label">
                                            <InputText id="linkFreq" {...input} />
                                        </span>
                                    </div>
                                )}
                            />
                            <Field
                                name="linkBaud"
                                render={({ input }) => (
                                    <div className="field">
                                        <span>Link baud</span>
                                        <span className="p-float-label">
                                            <InputText id="linkBaud" {...input} />
                                        </span>
                                    </div>
                                )}
                            />
                            <div className="submit-buttons-in-modal">
                                <Button
                                    label="Cancel"
                                    onClick={() => setDialogOpen(false)}
                                    icon="pi pi-times"
                                    type="button"
                                />
                                <Button label="Submit" icon="pi pi-check" type="submit" />
                            </div>
                        </form>
                    )}
                />
            </Dialog>
        </div>
    );
};
