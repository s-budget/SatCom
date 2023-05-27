import { Button } from "primereact/button";
import { useNavigate } from "react-router-dom";
import "./HomePage.css";
import { Fade } from "react-awesome-reveal";
import { userHasRole, UserRole } from "../../models/userData";
import { useSelector } from "react-redux";
import { AppState } from "../../store/configureStore";

export const HomePage = () => {
    const navigate = useNavigate();
    const user = useSelector((state: AppState) => state.user);

    return (
        <div className="home-container">
            <section className="home-page-section background-photo-section">
                <img src="/space.png" alt="space" className="background-img" />
                <div className="overlay-home-page">
                    <div className="background-photo-container">
                        <h1>SATCOM</h1>
                        <h3>
                            SATCom - a solution to easily create new satellites and send messages to
                            existing ones.
                        </h3>
                    </div>
                </div>
            </section>
            <section className="home-page-section options-section">
                <h2>What does SATCom offer?</h2>
                <div className="options-container">
                    <Fade direction="up" triggerOnce>
                        <div>
                            <div className="icon-size">
                                <i className="fas fa-message" />
                            </div>
                            <div>
                                <p>Communication with satellites</p>
                            </div>
                            <Button
                                label="Send Message"
                                onClick={() => navigate("/send-message")}
                                className="button-width-home-page"
                            />
                        </div>
                    </Fade>
                </div>
            </section>
            {userHasRole(user, UserRole.SuperAdmin) && (
                <section className="home-page-section options-section">
                    <div className="options-container">
                        <Fade direction="up" triggerOnce>
                            <div>
                                <div className="icon-size">
                                    <i className="fas fa-user" />
                                </div>
                                <div>
                                    <p>See existing users</p>
                                </div>
                                <Button
                                    label="Users"
                                    onClick={() => navigate("/users")}
                                    className="button-width-home-page"
                                />
                            </div>
                        </Fade>
                    </div>
                </section>
            )}
            {userHasRole(user, UserRole.SatelliteAdmin) && (
                <>
                    <section className="home-page-section options-section">
                        <div className="options-container">
                            <Fade direction="up" triggerOnce>
                                <div>
                                    <div className="icon-size">
                                        <i className="fas fa-satellite" />
                                    </div>
                                    <div>
                                        <p>See all satellites</p>
                                    </div>
                                    <Button
                                        label="Satellites"
                                        onClick={() => navigate("/satellites")}
                                        className="button-width-home-page"
                                    />
                                </div>
                            </Fade>
                        </div>
                    </section>
                    <section className="home-page-section options-section">
                        <div className="options-container">
                            <Fade direction="up" triggerOnce>
                                <div>
                                    <div className="icon-size">
                                        <i className="fas fa-wifi" />
                                    </div>
                                    <div>
                                        <p>See all links</p>
                                    </div>
                                    <Button
                                        label="Links"
                                        onClick={() => navigate("/links")}
                                        className="button-width-home-page"
                                    />
                                </div>
                            </Fade>
                        </div>
                    </section>
                    <section className="home-page-section options-section">
                        <div className="options-container">
                            <Fade direction="up" triggerOnce>
                                <div>
                                    <div className="icon-size">
                                        <i className="fas fa-satellite-dish" />
                                    </div>
                                    <div>
                                        <p>See all transmitters</p>
                                    </div>
                                    <Button
                                        label="Transmitters"
                                        onClick={() => navigate("/transmitters")}
                                        className="button-width-home-page"
                                    />
                                </div>
                            </Fade>
                        </div>
                    </section>
                </>
            )}
        </div>
    );
};
