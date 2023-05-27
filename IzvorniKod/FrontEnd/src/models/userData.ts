export interface UserData {
    password?: string;
    email?: string;
    username?: string;
    role?: UserRole;
    id?: number;
    locale?: Locale;
    token?: string;
    passwordRecoveryToken?: string;
}

export enum UserRole {
    SuperAdmin = "SUPER_ADMIN", 
    SatelliteAdmin = "SATELLITE_ADMIN", 
    User = "USER"
}

export const userHasRole = (user: UserData, role: UserRole) => {
    return user.role ? user.role === role : false;
}
export const userHasId = (user: UserData, idDelete: number) => {
    return user.id ? user.id === idDelete : false
}
export enum Locale {
    en = 0,
    hr = 1,
}