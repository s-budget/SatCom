import { Button, Column, ConfirmDialog, DataTable } from "primereact";
import { useCallback, useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { deleteUser, getAllUsers } from "../../api/users";
import { UserData, userHasId, userHasRole, UserRole } from "../../models/userData";
import { InputText } from "primereact/inputtext";
import { FilterMatchMode } from "primereact/api";
import "./DataTableDemo.css";
import "./Users.css";

import { useSelector } from "react-redux";
import { AppState } from "../../store/configureStore";
import { showToastMessage } from "../../actions/toastMessageActions";
import { useNavigate } from "react-router-dom";

const cols = [
    { field: "username", header: "Name", sortable: false },
    { field: "email", header: "Email", sortable: true },
    { field: "role", header: "Role", sortable: false },
];

export const Users = () => {
    const user = useSelector((state: AppState) => state.user);
    const navigate = useNavigate();
    const [userList, setUserList] = useState<UserData[]>([]);
    const [openDeleteDialog, setOpenDeleteDialog] = useState(false);
    const [userId, setUserId] = useState(-1);

    const dispatch = useDispatch();
    const fetchUsers = useCallback(async () => {
        try {
            const res = await getAllUsers();
            setUserList(res);
        } catch (error) {
            dispatch(showToastMessage("An error has occurred while fetching all users.", "error"));
        }
    }, [dispatch]);

    useEffect(() => {
        fetchUsers();
    }, [fetchUsers]);

    const handleDeleteUser = async () => {
        try {
            await deleteUser(userId);
            dispatch(showToastMessage("User successfully deleted.", "success"));
            fetchUsers();
            setUserId(-1);
        } catch (error) {
            dispatch(showToastMessage("An error has occurred while deleting a user.", "error"));
        } finally {
            setOpenDeleteDialog(false);
        }
    };

    const actionDeleteUser = (id: number) => {
        let korisnik = false;
        if (userHasId(user, id)) {
            korisnik = true;
        }
        return (
            <Button
                icon="fa fa-trash"
                className="p-button-outlined"
                disabled={korisnik}
                onClick={() => {
                    if (!userHasId(user, id)) {
                        setUserId(id);
                        setOpenDeleteDialog(true);
                    }
                }}
            ></Button>
        );
    };

    const [filters2, setFilters2] = useState({
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
        username: { value: null, matchMode: FilterMatchMode.CONTAINS },
        email: { value: null, matchMode: FilterMatchMode.CONTAINS },
        role: { value: null, matchMode: FilterMatchMode.CONTAINS },
        userId: { value: null, matchMode: FilterMatchMode.CONTAINS },
    });

    const [globalFilterValue2, setGlobalFilterValue2] = useState("");
    const onGlobalFilterChange2 = (e: { target: { value: any } }) => {
        const value = e.target.value;
        let _filters2 = { ...filters2 };
        _filters2["global"].value = value;

        setFilters2(_filters2);
        setGlobalFilterValue2(value);
    };
    const renderHeader2 = () => {
        return (
            <div>
                <div className="flex align-items-center export-buttons"></div>
                <div className="flex justify-content-end">
                    <span className="p-input-icon-left">
                        <i className="pi pi-search" />
                        <InputText
                            value={globalFilterValue2}
                            onChange={onGlobalFilterChange2}
                            placeholder="Keyword Search"
                        />
                    </span>
                </div>
            </div>
        );
    };

    const header2 = renderHeader2();

    return (
        <div>
            <h1 className="users-container">USERS</h1>
            <div className="add-user-button">
                {userHasRole(user, UserRole.SuperAdmin) && (
                    <Button label="Add User" onClick={() => navigate("/user-creation")} />
                )}
            </div>

            <DataTable
                resizableColumns
                showGridlines
                value={userList}
                emptyMessage={"No results yet."}
                paginator
                className="p-datatable-customers"
                rows={10}
                responsiveLayout="stack"
                dataKey="userId"
                filters={filters2}
                filterDisplay="row"
                globalFilterFields={["username", "email", "role", "userId"]}
                header={header2}
                onRowClick={rowData => {}}
            >
                {cols.map(col => {
                    return (
                        <Column
                            key={col.field}
                            field={col.field}
                            header={col.header}
                            sortable={col.sortable}
                            filter
                            filterPlaceholder={"Search by " + col.field}
                            style={{ minWidth: "12rem" }}
                        />
                    );
                })}
                <Column
                    key={"Delete"}
                    field={"userId"}
                    header={"Delete"}
                    body={user => actionDeleteUser(user.userId)}
                ></Column>
            </DataTable>
            <ConfirmDialog
                visible={openDeleteDialog}
                onHide={() => setOpenDeleteDialog(false)}
                message="Are you sure you want to delete user?"
                header="Delete Confirmation"
                icon="pi pi-exclamation-triangle"
                accept={handleDeleteUser}
                acceptClassName={"p-button-danger"}
                rejectClassName={"reject-button"}
            />
        </div>
    );
};
