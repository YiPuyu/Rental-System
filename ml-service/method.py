from fastapi import FastAPI
import pandas as pd
import joblib

app = FastAPI()

# 加载训练好的模型和编码器
model = joblib.load('/Users/apple/PycharmProjects/PythonProject1/.venv/bin/rent_model.pkl')
encoder = joblib.load('/Users/apple/PycharmProjects/PythonProject1/.venv/bin/rent_encoder.pkl')


@app.post("/predict")
def predict_rent(data: dict):
    # 把输入字典转换成 DataFrame
    df = pd.DataFrame([data])

    try:
        # 用训练时的 encoder 做 one-hot 编码
        X_encoded = encoder.transform(df)
        # 预测租金
        pred = model.predict(X_encoded)
        return {"predicted_rent": float(pred[0])}
    except Exception as e:
        # 捕获错误返回，方便调试
        return {"error": str(e)}
