import { Button } from "primereact/button";
import { Column } from "primereact/column";
import { DataTable } from "primereact/datatable";
import { Dialog } from "primereact/dialog";
import { useCallback, useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { createNewSatelite, getAllSatelites } from "../../api/satelite";
import { ISatelite, sateliteInit } from "../../models/satelite";
import { Field, Form } from "react-final-form";
import { InputText } from "primereact/inputtext";
import "./Satelites.css";
import { userHasRole, UserRole } from "../../models/userData";
import { AppState } from "../../store/configureStore";
import { showToastMessage } from "../../actions/toastMessageActions";

const cols = [
    { field: "satName", header: "Name", sortable: true },
    { field: "createdBy", header: "Created by", sortable: true },
    { field: "creationDate", header: "Creation date", sortable: false },
];

export const Satelites = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const [satellites, setSatellites] = useState<ISatelite[]>([]);
    const [dialogOpen, setDialogOpen] = useState(false);
    const user = useSelector((state: AppState) => state.user);

    const fetchSatelites = useCallback(async () => {
        try {
            const res = await getAllSatelites();
            setSatellites(res);
        } catch (error) {
            dispatch(
                showToastMessage("An error has occurred while fetching all satellites.", "error")
            );
        }
    }, [dispatch]);

    useEffect(() => {
        fetchSatelites();
    }, [fetchSatelites]);

    const handleAddNewSatelite = async (data: ISatelite) => {
        try {
            await createNewSatelite(data);
            fetchSatelites();
            dispatch(showToastMessage("Satellite successfully created.", "success"));
        } catch (error) {
            dispatch(
                showToastMessage("An error has occurred while adding a new satellite.", "error")
            );
        } finally {
            setDialogOpen(false);
        }
    };

    return (
        <div className="satelites-container">
            <h1 className="sat-title">SATELLITES</h1>
            <div className="satelite-datatable">
                <div className="satelite-add-new">
                    <h3>Click one row to see more information!</h3>
                    {userHasRole(user, UserRole.SatelliteAdmin) && (
                        <Button label="Add New Satellite" onClick={() => setDialogOpen(true)} />
                    )}
                </div>
                <DataTable
                    resizableColumns
                    showGridlines
                    value={satellites}
                    emptyMessage={"No results yet."}
                    responsiveLayout="stack"
                    onRowClick={rowData => {
                        navigate("/satallite-details", {
                            state: {
                                satelite: satellites.find(x => x.satName === rowData.data.satName),
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
                <Dialog
                    visible={dialogOpen}
                    id="new-satelite"
                    header="Add New Satellite"
                    draggable={false}
                    onHide={() => setDialogOpen(false)}
                >
                    <Form
                        onSubmit={(data: ISatelite) => handleAddNewSatelite(data)}
                        initialValues={sateliteInit}
                        render={({ handleSubmit }) => (
                            <form
                                id="new-satelite"
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
            </div>
        </div>
    );
};
