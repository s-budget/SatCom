import { Button } from "primereact/button";
import { Column } from "primereact/column";
import { DataTable } from "primereact/datatable";
import { Dialog } from "primereact/dialog";
import { useCallback, useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { Field, Form } from "react-final-form";
import { InputText } from "primereact/inputtext";
import { userHasRole, UserRole } from "../../models/userData";
import { AppState } from "../../store/configureStore";
import { ILink, linkInit } from "../../models/link";
import { createNewLink, getAllLinks } from "../../api/links";
import "./Links.css";
import { showToastMessage } from "../../actions/toastMessageActions";
import { getAllModes } from "../../api/mode";
import { Dropdown } from "primereact";

const cols = [
    { field: "linkMode", header: "Link mode", sortable: false },
    { field: "linkFreq", header: "Link frequency", sortable: true },
    { field: "linkBaud", header: "Link baud", sortable: false },
];

export const Links = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const [links, setLinks] = useState<ILink[]>([]);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [modeOptions, setModeOptions] = useState([]);
    const user = useSelector((state: AppState) => state.user);

    const fetchLinks = useCallback(async () => {
        try {
            const res = await getAllLinks();
            setLinks(res);
        } catch (error) {
            dispatch(showToastMessage("An error has occurred while fetching all links.", "error"));
        }
    }, [dispatch]);

    
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
        fetchLinks();
        fetchModes();
    }, [fetchLinks, fetchModes]);

    const handleAddNewLink = async (data: ILink) => {
        try {
            await createNewLink(data);
            dispatch(showToastMessage("Link successfully created.", "success"));
            fetchLinks();
        } catch (error) {
            dispatch(showToastMessage("An error has occurred while adding a new link.", "error"));
        } finally {
            setDialogOpen(false);
        }
    };

    return (
        <div className="link-container">
            <h1 className="link-title">LINKS</h1>
            <div className="link-datatable">
                <div className="link-add-new">
                    <h3>Click one row to see more information!</h3>
                    {userHasRole(user, UserRole.SatelliteAdmin) && (
                        <Button label="Add New Link" onClick={() => setDialogOpen(true)} />
                    )}
                </div>
                <DataTable
                    resizableColumns
                    showGridlines
                    value={links}
                    emptyMessage={"No results yet."}
                    responsiveLayout="stack"
                    onRowClick={rowData => {
                        navigate("/link-details", {
                            state: {
                                link: links.find(x => x.linkId === rowData.data.linkId),
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
                    id="new-link"
                    header="Add New Link"
                    draggable={false}
                    onHide={() => setDialogOpen(false)}
                >
                    <Form
                        onSubmit={(data: ILink) => handleAddNewLink(data)}
                        initialValues={linkInit}
                        render={({ handleSubmit }) => (
                            <form
                                id="new-link"
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
        </div>
    );
};
