import { Button, Column, ConfirmDialog, DataTable, FilterMatchMode, InputText } from "primereact";
import { useCallback, useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import "./MessageHistory.css";
import { deleteAllMessages, deleteMessage, getAllMessages } from "../../api/messages";
import { AppState } from "../../store/configureStore";
import { showToastMessage } from "../../actions/toastMessageActions";

const cols = [
    { field: "text", header: "Message", sortable: false },
    { field: "satelliteName", header: "Satellite Name", sortable: false },
    { field: "stationName", header: "Station Name", sortable: false },
    { field: "creationDate", header: "Creation Date", sortable: false },
    { field: "direction", header: "Direction", sortable: false },
];
const messages = [
    {
        text: "e buraz",
        satelliteName: "Burki 1",
        stationName: "Burki 2",
        creationDate: "xxx",
        direction: "UPLOAD",
    },
];

export const MessageHistory = () => {
    const user = useSelector((state: AppState) => state.user);
    const [messageList, setMessageList] = useState([]);
    const dispatch = useDispatch();
    const [filterValue, setFilterValue] = useState("");
    const [openDeleteAllDialog, setOpenDeleteAllDialog] = useState(false);
    const [openDeleteOneDialog, setOpenDeleteOneDialog] = useState(false);
    const [messageId, setMessageId] = useState('');
    const [filters, setFilters] = useState({
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
    });

    const fetchMessageHistory = useCallback(async () => {
        try {
            const userId: number = user.id!;
            const res = await getAllMessages();
            res.sort((a: any, b: any) => Date.parse(b.creationDate) - Date.parse(a.creationDate));
            setMessageList(res);
        } catch (error) {
            dispatch(
                showToastMessage("An error has occurred while fetching all messages.", "error")
            );
        }
    }, [dispatch]);

    useEffect(() => {
        fetchMessageHistory();
    }, [fetchMessageHistory]);

    const onFilterChange = (event: any) => {
        const value = event.target.value;
        let _filters = { ...filters };
        _filters["global"].value = value;

        setFilters(_filters);
        setFilterValue(value);
    };

    const handleDeleteAllMessages = async () => {
        const response = await deleteAllMessages();
        setMessageList(response);
    };

    const actionDeleteMessage = (id: string) => {
        return (
            <Button
                icon="fa fa-trash"
                className="p-button-outlined"
                onClick={() => {
                        setMessageId(id);
                        setOpenDeleteOneDialog(true);
                }}
            ></Button>
        );
    };

    const handleDeleteMessage = async() => {
        try {
            const res = await deleteMessage(messageId);
            dispatch(showToastMessage("Message successfully deleted.", "success"));
            await fetchMessageHistory();
        } catch (error) {
            dispatch(showToastMessage("An error has occurred while deleting message.", "error"));
        } finally {
            setOpenDeleteOneDialog(false);
        }
    }

    const tableHeader = () => {
        return (
            <div className="table-header">
                <Button
                    label="Delete All"
                    className="p-button-danger"
                    onClick={e => {
                        e.preventDefault();
                        setOpenDeleteAllDialog(true);
                    }}
                />
                <span className="p-input-icon-left">
                    <i className="pi pi-search" />
                    <InputText
                        value={filterValue}
                        onChange={onFilterChange}
                        placeholder="Keyword Search"
                    />
                </span>
            </div>
        );
    };
    return (
        <div>
            <h1 className="message-title">MESSAGE HISTORY</h1>
            <DataTable
                className="message-table"
                resizableColumns
                showGridlines
                value={messageList}
                emptyMessage={"No results yet."}
                responsiveLayout="stack"
                onRowClick={rowData => {}}
                filters={filters}
                filterDisplay="menu"
                header={tableHeader}
                paginator
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown"
                rows={10}
                rowsPerPageOptions={[10, 20, 50]}
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
                <Column
                    key={"Delete"}
                    field={"userId"}
                    header={"Delete"}
                    body={message => actionDeleteMessage(message.messageId)}
                    className="delete-message"
                ></Column>
            </DataTable>
            <ConfirmDialog
                visible={openDeleteAllDialog}
                onHide={() => setOpenDeleteAllDialog(false)}
                message="Are you sure you want to delete all messages?"
                header="Delete Confirmation"
                icon="pi pi-exclamation-triangle"
                accept={handleDeleteAllMessages}
                acceptClassName={"p-button-danger"}
                rejectClassName={"reject-button"}
            />
            <ConfirmDialog
                visible={openDeleteOneDialog}
                onHide={() => setOpenDeleteOneDialog(false)}
                message="Are you sure you want to delete selected message?"
                header="Delete Confirmation"
                icon="pi pi-exclamation-triangle"
                accept={handleDeleteMessage}
                acceptClassName={"p-button-danger"}
                rejectClassName={"reject-button"}
            />
        </div>
    );
};
