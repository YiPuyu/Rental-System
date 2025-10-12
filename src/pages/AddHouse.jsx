import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function AddHouse() {
    const [title, setTitle] = useState("");
    const [address, setAddress] = useState("");
    const [city, setCity] = useState("");
    const [houseType, setHouseType] = useState("");
    const [rent, setRent] = useState("");
    const [description, setDescription] = useState("");
    const [predicting, setPredicting] = useState(false);

    const navigate = useNavigate();

    // 🧠 调用后端预测租金接口
    const handlePredictRent = async () => {
        if (!title || !address || !city || !houseType) {
            return alert("请先填写标题、地址、城市和房型");
        }

        setPredicting(true);
        try {
            const res = await fetch("http://localhost:8080/api/predict-rent", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    title,
                    address,
                    city,
                    houseType,
                    description,
                }),
            });

            if (res.ok) {
                const data = await res.json();
                setRent(data.predictedRent);
                alert(`预测租金为：${data.predictedRent} 元/月`);
            } else {
                alert("预测失败，请检查后端服务是否启动");
            }
        } catch (err) {
            console.error(err);
            alert("请求出错，请稍后再试");
        } finally {
            setPredicting(false);
        }
    };

    // 🏠 提交房源信息
    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!title || !address || !city || !houseType || !rent) {
            return alert("请填写完整信息");
        }
        if (Number(rent) <= 0) {
            return alert("租金必须大于 0");
        }

        const token = localStorage.getItem("token");
        if (!token) return alert("请先登录");

        const property = { title, address, city, houseType, rent: Number(rent), description };

        const res = await fetch("http://localhost:8080/api/properties", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`,
            },
            body: JSON.stringify(property),
        });

        if (res.ok) {
            alert("房源发布成功！");
            navigate("/houses");
        } else {
            let data;
            try {
                data = await res.json();
            } catch {
                data = {};
            }
            alert(data.error || `发布失败（${res.status}）`);
        }
    };

    return (
        <div className="p-6 max-w-xl mx-auto">
            <h2 className="text-2xl font-bold mb-4">发布新房源</h2>
            <form onSubmit={handleSubmit} className="grid gap-4">
                <input type="text" placeholder="标题" value={title} onChange={e => setTitle(e.target.value)} className="border p-2 rounded"/>
                <input type="text" placeholder="地址" value={address} onChange={e => setAddress(e.target.value)} className="border p-2 rounded"/>
                <input type="text" placeholder="城市" value={city} onChange={e => setCity(e.target.value)} className="border p-2 rounded"/>
                <input type="text" placeholder="房型" value={houseType} onChange={e => setHouseType(e.target.value)} className="border p-2 rounded"/>

                <div className="flex gap-2 items-center">
                    <input
                        type="number"
                        placeholder="租金"
                        value={rent}
                        onChange={e => setRent(e.target.value)}
                        className="border p-2 rounded flex-1"
                    />
                    <button
                        type="button"
                        onClick={handlePredictRent}
                        disabled={predicting}
                        className={`px-3 py-2 rounded text-white ${predicting ? "bg-gray-400" : "bg-green-500 hover:bg-green-600"}`}
                    >
                        {predicting ? "预测中..." : "预测租金"}
                    </button>
                </div>

                <textarea placeholder="描述" value={description} onChange={e => setDescription(e.target.value)} className="border p-2 rounded"/>
                <button type="submit" className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">发布房源</button>
            </form>
        </div>
    );
}
