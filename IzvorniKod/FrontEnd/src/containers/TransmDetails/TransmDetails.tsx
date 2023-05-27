import { useCallback, useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { Field, Form } from "react-final-form";
import { Button } from "primereact/button";
import { AppState } from "../../store/configureStore";
import { userHasRole, UserRole } from "../../models/userData";
import { Dialog } from "primereact/dialog";
import { Dropdown, InputText } from "primereact";
import { ITransmitter } from "../../models/transmitter";
import { deleteTransmitter, editTransmitter, getTransmitterById } from "../../api/transmitters";
import "./TransmDetails.css";
import { showToastMessage } from "../../actions/toastMessageActions";
import { getAllModes } from "../../api/mode";

interface ILocationState {
    transmitter: ITransmitter;
}

export const TransmDetails = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const user = useSelector((state: AppState) => state.user);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [sendedTransm, setSendedTransm] = useState(
        (location.state as ILocationState)?.transmitter
    );
    const dispatch = useDispatch();
    const [modeOptions, setModeOptions] = useState([]);

    const onSubmit = async (data: ITransmitter) => {
        if (!data) return;
        try {
            await editTransmitter(data.transmId ?? -1, data);
            const res = await getTransmitterById(sendedTransm.transmId ?? -1);
            setSendedTransm(res);
            dispatch(showToastMessage("Transmitter successfully edited.", "success"));
        } catch (error) {
            dispatch(showToastMessage("An error has occurred while editing transmitter.", "error"));
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

    const handleDeleteTransmitter = async (id: number) => {
        try {
            await deleteTransmitter(id);
            dispatch(showToastMessage("Transmitter successfully deleted.", "success"));
        } catch (error) {
            dispatch(showToastMessage("An error has occurred while deleting link.", "error"));
        } finally {
            navigate("/transmitters");
        }
    };

    return (
        <div className="transm-container">
            <div className="transm-options">
                <i
                    className={"fa fa-backward show-cursor back-action"}
                    onClick={() => navigate("/transmitters")}
                >
                    {"   "}B a c k
                </i>
                {userHasRole(user, UserRole.SatelliteAdmin) && (
                    <div className="options">
                        <Button
                            label="Delete transmitter"
                            className="delete-button"
                            onClick={() => {
                                handleDeleteTransmitter(sendedTransm.transmId ?? -1);
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
            <h1>Transmitter name: {sendedTransm.transmName}</h1>
            <h3>Transmitter baud: {sendedTransm.transmBaud}</h3>
            <h3>Transmitter frequency: {sendedTransm.transmFreq}</h3>
            <h3>Transmitter mode: {sendedTransm.transmMode}</h3>
            <hr />
            <h1>Satellite: {sendedTransm.owningSatellite}</h1>
            <Dialog
                visible={dialogOpen}
                id="edit-transm"
                header={"Edit transm details"}
                draggable={false}
                onHide={() => setDialogOpen(false)}
            >
                <Form
                    onSubmit={(data: ITransmitter) => onSubmit(data)}
                    initialValues={sendedTransm}
                    render={({ handleSubmit }) => (
                        <form
                            id="edit-transm"
                            onSubmit={handleSubmit}
                            className="form-container"
                            autoComplete="off"
                        >
                            <Field
                                name="transmName"
                                render={({ input }) => (
                                    <div className="field">
                                        <span>Transmitter name</span>
                                        <span className="p-float-label">
                                            <InputText id="transmName" {...input} />
                                        </span>
                                    </div>
                                )}
                            />
                            <Field
                                name="transmMode"
                                render={({ input }) => (
                                    <div className="field">
                                        <span>Transmitter mode</span>
                                        <span className="p-float-label">
                                            <Dropdown
                                                id="transmMode"
                                                {...input}
                                                options={modeOptions}
                                                virtualScrollerOptions={{ itemSize: 38 }}
                                            />
                                        </span>
                                    </div>
                                )}
                            />
                            <Field
                                name="transmBaud"
                                render={({ input }) => (
                                    <div className="field">
                                        <span>Transmitter baud</span>
                                        <span className="p-float-label">
                                            <InputText id="transmBaud" {...input} />
                                        </span>
                                    </div>
                                )}
                            />
                            <Field
                                name="transmFreq"
                                render={({ input }) => (
                                    <div className="field">
                                        <span>Transmitter frequency</span>
                                        <span className="p-float-label">
                                            <InputText id="transmFreq" {...input} />
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
