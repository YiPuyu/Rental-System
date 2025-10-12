import React from "react";

import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./pages/Login";
import HouseList from "./pages/HouseList";
import PropertyDetailPage from "./pages/PropertyDetailPage";
import RegisterPage from "./pages/Register";
import AddHouse from "./pages/AddHouse.jsx";

export default function App() {
    return (
        <Router>
            <Routes>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route
                    path="/houses"
                    element={
                        <PrivateRoute>
                            <HouseList />
                        </PrivateRoute>
                    }
                />
                <Route path="/add-house" element={<AddHouse />} />
                <Route path="/properties/:id" element={<PropertyDetailPage />} />
                <Route path="*" element={<Navigate to="/login" />} />
            </Routes>
        </Router>
    );
}

// 受保护路由组件
function PrivateRoute({ children }) {
    const token = localStorage.getItem("token");
    if (!token) {
        return <Navigate to="/login" replace />;
    }
    return children;
}
