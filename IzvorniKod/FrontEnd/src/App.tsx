import "./App.css";
import { HomePage } from "./containers/HomePage/HomePage";
import { Layout } from "./containers/Layout/Layout";
import { Satelites } from "./containers/Satelites/Satelites";
import { Route, Routes, useLocation } from "react-router-dom";
import { configureAxiosClient } from "./api/axiosClient";
import axios from "axios";
import { SateliteDetails } from "./containers/SateliteDetails/SateliteDetails";
import { Links } from "./containers/Links/Links";
import UserCreationForm from "./containers/UserCreationForm/userCreationForm";
import { Message } from "./containers/Message/Message";
import { Users } from "./containers/Users/Users";
import { Transmitters } from "./containers/Transmitters/Transmitters";
import { LinkDetails } from "./containers/LinkDetails/LinkDetails";
import { TransmDetails } from "./containers/TransmDetails/TransmDetails";
import { UserDetails } from "./containers/UserDetails/UserDetails";
import { MessageHistory } from "./containers/MessageHistory/MessageHistory";
configureAxiosClient(axios);

export const App = () => {
    const location = useLocation();
    return (
        <Layout>
            <Routes location={location}>
                <Route path="*" element={<HomePage />} />
                <Route path="/satellites" element={<Satelites />} />
                <Route path="/satallite-details" element={<SateliteDetails />} />
                <Route path="/links" element={<Links />} />
                <Route path="/link-details" element={<LinkDetails />} />
                <Route path="/transmitters" element={<Transmitters />} />
                <Route path="/transm-details" element={<TransmDetails />} />
                <Route path="/user-creation" element={<UserCreationForm />} />
                <Route path="/send-message" element={<Message />} />
                <Route path="/users" element={<Users />} />
                <Route path="/user-details" element={<UserDetails />} />
                <Route path="/message-history" element={<MessageHistory />} />
            </Routes>
        </Layout>
    );
};
