import { Column } from "primereact/column";
import { DataTable } from "primereact/datatable";
import { useCallback, useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import "./Transmitters.css";
import { ITransmitter } from "../../models/transmitter";
import { getAllTransmitters } from "../../api/transmitters";
import { showToastMessage } from "../../actions/toastMessageActions";

const cols = [
    { field: "transmName", header: "Transmitter name", sortable: false },
    { field: "transmBaud", header: "Transmitter baud", sortable: true },
    { field: "transmFreq", header: "Transmitter frequency", sortable: true },
    { field: "transmMode", header: "Transmitter mode", sortable: true },
];

export const Transmitters = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const [transmitters, setTransmitters] = useState<ITransmitter[]>([]);

    const fetchTransmitters = useCallback(async () => {
        try {
            const res = await getAllTransmitters();
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
        <div className="transmitter-container">
            <h1 className="transm-title">TRANSMITTERS</h1>
            <div className="transmitter-datatable">
                <div className="transmitter-add-new">
                    <h3>Click one row to see more information!</h3>
                </div>
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
        </div>
    );
};
