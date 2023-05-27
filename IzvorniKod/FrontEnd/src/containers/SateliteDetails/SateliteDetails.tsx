import { Column } from "primereact/column";
import { DataTable } from "primereact/datatable";
import { useCallback, useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { ISatelite} from "../../models/satelite";
import { useDispatch, useSelector } from "react-redux";
import { Field, Form } from "react-final-form";
import { Button } from "primereact/button";
import { deleteSatelite, editSatelite, getSateliteById } from "../../api/satelite";
import "./SateliteDetails.css";
import { AppState } from "../../store/configureStore";
import { userHasRole, UserRole } from "../../models/userData";
import { Dialog } from "primereact/dialog";
import { Dropdown, InputText } from "primereact";
import { ITransmitter, transmInit } from "../../models/transmitter";
import { createNewTransmitter, getTransmForSatellite } from "../../api/transmitters";
import { showToastMessage } from "../../actions/toastMessageActions";
import { getAllModes } from "../../api/mode";

interface ILocationState {
    satelite: ISatelite;
}

const cols = [
    { field: "transmId", header: "ID", sortable: true },
    { field: "transmName", header: "Transmitter name", sortable: false },
    { field: "transmBaud", header: "Transmitter baud", sortable: true },
    { field: "transmFreq", header: "Transmitter frequency", sortable: true },
    { field: "transmMode", header: "Transmitter mode", sortable: true },
];

export const SateliteDetails = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const user = useSelector((state: AppState) => state.user);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [dialogOpenTransm, setDialogOpenTransm] = useState(false);
    const [transmitters, setTransmitters] = useState<ITransmitter[]>([]);
    const [modeOptions, setModeOptions] = useState([]);
    const dispatch = useDispatch();

    const [sendedSatellite, setSendedSatellite] = useState(
        (location.state as ILocationState)?.satelite
    );

    const onSubmit = async (data: ISatelite) => {
        if (!data) return;
        try {
            await editSatelite(data.satId ?? -1, data);
            dispatch(showToastMessage("Satellite successfully edited.", "success"));
            const res = await getSateliteById(sendedSatellite.satId ?? -1);
            setSendedSatellite(res);
        } catch (error) {
            dispatch(showToastMessage("An error has occurred while editing satellite.", "error"));
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

    const handleDeleteSatelite = async (id: number) => {
        try {
            await deleteSatelite(id);
            dispatch(showToastMessage("Satellite successfully deleted.", "success"));
        } catch (error) {
            dispatch(showToastMessage("An error has occurred while deleting satellite.", "error"));
        } finally {
            navigate("/satellites");
        }
    };

    const handleAddNewTransmitter = async (data: ITransmitter) => {
        console.log(data);
        try {
            await createNewTransmitter(data, sendedSatellite.satId ?? -1);
            fetchTransmitters();
            dispatch(showToastMessage("Transmitter successfully created.", "success"));
        } catch (error) {
            dispatch(
                showToastMessage("An error has occurred while adding a new transmitter.", "error")
            );
        } finally {
            setDialogOpenTransm(false);
        }
    };

    const fetchTransmitters = useCallback(async () => {
        try {
            const res = await getTransmForSatellite(sendedSatellite.satId ?? -1);
            setTransmitters(res);
        } catch (error) {
            dispatch(
                showToastMessage("An error has occurred while fetching all transmitters.", "error")
            );
        }
    }, [dispatch]);

    useEffect(() => {
        fetchTransmitters();
    }, [fetchTransmitters]);

    return (
        <div className="satelite-container">
            <div className="satellite-options">
                <i
                    className={"fa fa-backward show-cursor back-action"}
                    onClick={() => navigate("/satellites")}
                >
                    {"   "}B a c k
                </i>
                {userHasRole(user, UserRole.SatelliteAdmin) && (
                    <div className="options">
                        <Button
                            label="Delete satellite"
                            className="delete-button"
                            onClick={() => {
                                handleDeleteSatelite(sendedSatellite.satId ?? -1);
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
            <h1>{sendedSatellite.satName}</h1>
            {userHasRole(user, UserRole.SatelliteAdmin) && (
                <Button
                    className="button-add-new-transm-in-satelite-details"
                    label="Add New Transmitter"
                    onClick={() => setDialogOpenTransm(true)}
                />
            )}
            <div>
                <h3>Transmitters</h3>
                <DataTable
                    resizableColumns
                    showGridlines
                    value={transmitters}
                    emptyMessage={"No results yet."}
                    responsiveLayout="stack"
                    onRowClick={rowData => {
                        navigate("/transm-details", {
                            state: {
                                transmitter: transmitters.find(
                                    x => x.transmId === rowData.data.transmId
                                ),
                            },
                        });
                    }}
                >
                    {cols.map(col => {
                        return (
                            <Column
                                key={col.field}
                                field={col.field}
                                header={col.header}
                                sortable={col.sortable}
                            />
                        );
                    })}
                </DataTable>
            </div>
            <Dialog
                visible={dialogOpen}
                id="edit-satellite"
                header={`Edit satellite:  ${sendedSatellite.satName}`}
                draggable={false}
                onHide={() => setDialogOpen(false)}
            >
                <Form
                    onSubmit={(data: ISatelite) => onSubmit(data)}
                    initialValues={sendedSatellite}
                    render={({ handleSubmit }) => (
                        <form
                            id="edit-satellite"
                            onSubmit={handleSubmit}
                            className="form-container"
                            autoComplete="off"
                        >
                            <Field
                                name="satName"
                                render={({ input }) => (
                                    <div className="field">
                                        <span>Satellite Name</span>
                                        <span className="p-float-label">
                                            <InputText id="satName" {...input} />
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
            <Dialog
                visible={dialogOpenTransm}
                id="new-transmitter"
                header="Add New Transmitter"
                draggable={false}
                onHide={() => setDialogOpenTransm(false)}
            >
                <Form
                    onSubmit={(data: ITransmitter) => handleAddNewTransmitter(data)}
                    initialValues={transmInit}
                    render={({ handleSubmit }) => (
                        <form
                            id="new-transmitter"
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
                                    onClick={() => setDialogOpenTransm(false)}
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
