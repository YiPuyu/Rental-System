import React from "react";
import { useNavigate } from "react-router-dom";

export function LogoutButton() {
    const navigate = useNavigate();

    const handleLogout = () => {
        // 清除本地 token
        localStorage.removeItem("token");
        alert("已登出");
        navigate("/login");
    };

    return (
        <button onClick={handleLogout}>
            登出
        </button>
    );
}
