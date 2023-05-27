import {
    Button,
    Column,
    DataTable,
    Dialog,
    FilterMatchMode,
    InputText,
    InputTextarea,
    ProgressSpinner,
} from "primereact";
import { useCallback, useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { showToastMessage } from "../../actions/toastMessageActions";
import { getCompatibileLinks } from "../../api/links";
import { createMessage } from "../../api/messages";
import { getAllSatelites, getSatelliteTransmitters } from "../../api/satelite";
import { getLinkStations, getIsPresent } from "../../api/stations";
import "./Message.css";

const satelliteCols = [
    { field: "satName", header: "Satellite Name" },
    { field: "creationDate", header: "Creation Date" },
];
const linkCols = [
    { field: "linkMode", header: "Link Mode" },
    { field: "linkFreq", header: "Link Frequency" },
    { field: "linkBaud", header: "Link Baud" },
];

const transmitterCols = [
    { field: "transmName", header: "Transmitter Name" },
    { field: "transmMode", header: "Transmitter Mode" },
    { field: "transmFreq", header: "Transmitter Frequency" },
    { field: "transmBaud", header: "Transmitter Baud" },
];

const stationCols = [
    { field: "statName", header: "Station Name" },
    { field: "longitude", header: "Longitude" },
    { field: "latitude", header: "Latitude" },
    { field: "altitude", header: "Altitude" },
    { field: "successRate", header: "Success Rate" },
];

export const Message = () => {
    const [satelliteList, setSatelliteList] = useState([]);
    const [linkList, setLinkList] = useState([]);
    const [stationList, setStationList] = useState([]);
    const [transmitterList, setTransmitterList] = useState([]);

    const [selectedSatellite, setSelectedSatellite] = useState("");
    const [selectedLink, setSelectedLink] = useState("");
    const [selectionMode, setSelectionMode] = useState("");
    const [selectedStation, setSelectedStation] = useState({
        statId: 91,
        longitude: -2.02,
        latitude: 50.77,
        altitude: 60,
        statName: "M0EYT / 2E0NOG",
        successRate: 233349,
        antennas: [
            {
                antennaId: 23,
                antennaFreqHigh: 300000000,
                antennaFreqLow: 30000000,
                antennaType: "quadrafilar",
                linkAntenna: ["UHF"],
                stations_antennas: ["M0EYT / 2E0NOG"],
            },
            {
                antennaId: 24,
                antennaFreqHigh: 3000000000,
                antennaFreqLow: 300000000,
                antennaType: "eggbeater",
                linkAntenna: ["UHF"],
                stations_antennas: ["M0EYT / 2E0NOG"],
            },
        ],
    });

    const [visibleTransmitterModal, setVisibleTransmitterModal] = useState(false);
    const [message, setMessage] = useState("");
    const [step, setStep] = useState(1);
    const [filterValue, setFilterValue] = useState("");
    const [filters, setFilters] = useState({
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
    });
    const [loading, setLoading] = useState(false);
    const [satelliteResponse, setSatelliteResponse] = useState("");
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const fetchSatellites = useCallback(async () => {
        try {
            const res = await getAllSatelites();
            setSatelliteList(res);
        } catch (error) {
            dispatch(
                showToastMessage("An error has occurred while fetching all satellites.", "error")
            );
        }
    }, [dispatch]);

    useEffect(() => {
        fetchSatellites();
    }, [fetchSatellites]);

    const getCompatibleTransmitters = async (satId: number) => {
        const transmitters = await getSatelliteTransmitters(satId);
        setTransmitterList(transmitters);
    };

    const handleStep1 = async () => {
        setStep(2);
        getCompatibleTransmitters((selectedSatellite as any).satId);
        const links = await getCompatibileLinks((selectedSatellite as any).satId);
        setLinkList(links);
    };

    const handleStep2 = async () => {
        const stations = await getLinkStations((selectedLink as any).linkId);
        setStationList(stations);
        setStep(3);
    };

    const handleStep3 = () => {
        if (selectionMode == "automatic") {
            let max = -1;
            let ind = -1;
            (stationList as any[]).forEach((x, index) => {
                if (x.successRate > max) {
                    max = x.successRate;
                    ind = index;
                }
            });

            setSelectedStation(stationList[ind]);
        }

        if (selectedStation == null) {
            dispatch(
                showToastMessage(
                    "An error has occurred while selecting the station. There are no compatible stations.",
                    "error"
                )
            );
            setStep(1);
        } else {
            setStep(4);
        }
    };

    const handleStep4 = async () => {
        setLoading(true);

        const isPresent = await getIsPresent((selectedStation as any).statId);

        if (isPresent) {
            try {
                let response = await createMessage(
                    (selectedStation as any).statName,
                    (selectedSatellite as any).satName,
                    message,
                    (selectedLink as any).linkFreq,
                    (selectedLink as any).linkMode,
                    (selectedLink as any).linkBaud
                );
                dispatch(showToastMessage("Message successfully sent.", "success"));
                navigate("/message-history");
            } catch (error) {
                dispatch(
                    showToastMessage("An error has ocurred while sending the message.", "error")
                );
            }
        } else {
            dispatch(showToastMessage("Current station is not present in database.", "warn"));
        }

        setLoading(false);
    };

    const onFilterChange = (event: any) => {
        const value = event.target.value;
        let _filters = { ...filters };
        _filters["global"].value = value;

        setFilters(_filters);
        setFilterValue(value);
    };

    const stationTableHeader = () => {
        return (
            <div className="stat-table-header">
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

    const finishSendMessage = () => {
        setSelectedStation({
            statId: 91,
            longitude: -2.02,
            latitude: 50.77,
            altitude: 60,
            statName: "M0EYT / 2E0NOG",
            successRate: 233349,
            antennas: [
                {
                    antennaId: 23,
                    antennaFreqHigh: 300000000,
                    antennaFreqLow: 30000000,
                    antennaType: "quadrafilar",
                    linkAntenna: ["UHF"],
                    stations_antennas: ["M0EYT / 2E0NOG"],
                },
                {
                    antennaId: 24,
                    antennaFreqHigh: 3000000000,
                    antennaFreqLow: 300000000,
                    antennaType: "eggbeater",
                    linkAntenna: ["UHF"],
                    stations_antennas: ["M0EYT / 2E0NOG"],
                },
            ],
        });
        setSelectionMode("");
        setSelectedLink("");
        setFilterValue("");
        setMessage("");
        setSelectedSatellite("");
        setStep(1);
    };

    return (
        <div>
            <h1 className="message-title">SEND MESSAGE</h1>
            {step != 1 && (
                <div className="communication-params-container">
                    <div className="params-header">
                        <h2>Communication Parameters</h2>
                        <div>
                            <Button
                                label="See Transmitters"
                                onClick={() => setVisibleTransmitterModal(true)}
                            ></Button>
                        </div>
                    </div>
                    <div className="params">
                        <div className="satellite-data">
                            <div className="data">
                                <h4>Satellite Name: </h4>
                                <span>{(selectedSatellite as any).satName}</span>
                            </div>
                            <div className="data">
                                <h4>Creation Date:</h4>
                                <span>{(selectedSatellite as any).creationDate}</span>
                            </div>
                        </div>
                        {step > 2 && (
                            <div className="link-data">
                                <div className="data">
                                    <h4>Link Frequency:</h4>
                                    <span>{(selectedLink as any).linkFreq}</span>
                                </div>
                                <div className="data">
                                    <h4>Link Mode:</h4>
                                    <span>{(selectedLink as any).linkMode}</span>
                                </div>
                            </div>
                        )}
                        {step > 3 && (
                            <div className="station-data">
                                <div className="data">
                                    <h4>Station Name: </h4>
                                    <span>{(selectedStation as any).statName}</span>
                                </div>
                                <div className="data">
                                    <h4>Station Success Rate: </h4>
                                    <span>{(selectedStation as any).successRate}</span>
                                </div>
                            </div>
                        )}
                    </div>

                    <Dialog
                        visible={visibleTransmitterModal}
                        header="Compatible Transmitter List"
                        onHide={() => setVisibleTransmitterModal(false)}
                    >
                        <DataTable
                            resizableColumns
                            showGridlines
                            value={transmitterList}
                            emptyMessage={"No results yet."}
                            responsiveLayout="stack"
                        >
                            {transmitterCols.map(col => {
                                return (
                                    <Column key={col.field} field={col.field} header={col.header} />
                                );
                            })}
                        </DataTable>
                    </Dialog>
                </div>
            )}
            {step == 1 && (
                <div className="step-1">
                    <h3>Step 1: Select Satellite From List</h3>
                    <DataTable
                        resizableColumns
                        showGridlines
                        value={satelliteList}
                        emptyMessage={"No results yet."}
                        responsiveLayout="stack"
                        selectionMode="single"
                        selection={selectedSatellite}
                        onSelectionChange={e => setSelectedSatellite(e.value)}
                    >
                        {satelliteCols.map(col => {
                            return <Column key={col.field} field={col.field} header={col.header} />;
                        })}
                    </DataTable>
                    <div className="button-container">
                        <Button
                            label="Next step"
                            onClick={handleStep1}
                            disabled={selectedSatellite.length == 0}
                        ></Button>
                    </div>
                </div>
            )}

            {step == 2 && (
                <div className="step-2">
                    <h3>Step 2: Select Link From List</h3>
                    <DataTable
                        resizableColumns
                        showGridlines
                        value={linkList}
                        emptyMessage={"No results yet."}
                        responsiveLayout="stack"
                        selectionMode="single"
                        selection={selectedLink}
                        onSelectionChange={e => setSelectedLink(e.value)}
                    >
                        {linkCols.map(col => {
                            return <Column key={col.field} field={col.field} header={col.header} />;
                        })}
                    </DataTable>
                    <div className="button-container">
                        <Button
                            label="Previous Step"
                            onClick={() => {
                                setStep(1);
                            }}
                        ></Button>
                        <Button
                            label="Next Step"
                            onClick={handleStep2}
                            disabled={selectedLink.length == 0}
                        ></Button>
                    </div>
                </div>
            )}

            {step == 3 && (
                <div className="step-3">
                    <h3>Step 3: Select Selection Mode</h3>
                    <div className="selection-container">
                        <div
                            className={
                                selectionMode != "manual"
                                    ? "selection-option"
                                    : "selection-option selected"
                            }
                        >
                            <h3>Manual Selection</h3>
                            <p>This mode allows you to manually select the earth station.</p>
                            <Button
                                label="Select Manual"
                                onClick={() => {
                                    setSelectionMode("manual");
                                }}
                            ></Button>
                            {selectionMode == "manual" && (
                                <i className="pi pi-check-circle checked"></i>
                            )}
                        </div>
                        <div
                            className={
                                selectionMode !== "automatic"
                                    ? "selection-option"
                                    : "selection-option selected"
                            }
                        >
                            <h3>Automatic Selection</h3>

                            <p>
                                The application selects the optimal earth station based on an
                                algorithm.
                            </p>
                            <Button
                                label="Select Automatic"
                                onClick={() => setSelectionMode("automatic")}
                            ></Button>
                            {selectionMode == "automatic" && (
                                <i className="pi pi-check-circle checked"></i>
                            )}
                        </div>
                    </div>
                    {selectionMode == "manual" && (
                        <DataTable
                            className="station-table"
                            resizableColumns
                            showGridlines
                            value={stationList}
                            paginator
                            paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown"
                            rows={10}
                            rowsPerPageOptions={[10, 20, 50]}
                            emptyMessage={"No results yet."}
                            responsiveLayout="stack"
                            selectionMode="single"
                            selection={selectedStation}
                            filters={filters}
                            filterDisplay="menu"
                            header={stationTableHeader}
                            onSelectionChange={e => {
                                setSelectedStation(e.value);
                            }}
                            onRowClick={rowData => {}}
                        >
                            {stationCols.map(col => {
                                return (
                                    <Column key={col.field} field={col.field} header={col.header} />
                                );
                            })}
                        </DataTable>
                    )}
                    <div className="button-container">
                        <Button label="Previous Step" onClick={() => setStep(2)}></Button>
                        <Button
                            label="Next Step"
                            disabled={
                                selectionMode.length == 0 ||
                                (selectionMode == "manual" && selectedStation == null)
                            }
                            onClick={handleStep3}
                        ></Button>
                    </div>
                </div>
            )}

            {step == 4 && (
                <div className="step-4">
                    <h3>Step 4: Write Your Message To {(selectedSatellite as any).satName}</h3>
                    <div className="message-container">
                        <h3>Message:</h3>
                        <InputTextarea
                            value={message}
                            onChange={e => setMessage(e.target.value)}
                        ></InputTextarea>
                    </div>
                    {loading && (
                        <div className="progress-spinner">
                            <ProgressSpinner />
                        </div>
                    )}
                    <div className="button-container">
                        <Button label="Previous Step" onClick={() => setStep(3)}></Button>
                        <Button label="Send Message" onClick={handleStep4}></Button>
                    </div>
                </div>
            )}
        </div>
    );
};
