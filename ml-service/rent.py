import pandas as pd
import numpy as np
from sklearn.preprocessing import OneHotEncoder
from sklearn.linear_model import LinearRegression

# 设置随机种子
np.random.seed(42)
n = 500

# 城市和房型
cities = ['Osaka', 'Tokyo', 'Nagoya', 'Yokohama', 'Sapporo']
house_types = ['Apartment', 'Studio', 'House']

# 随机生成城市和房型
city = np.random.choice(cities, size=n)
houseType = np.random.choice(house_types, size=n, p=[0.5, 0.4, 0.1])

# 租金基于城市和房型估算（美元/月）
base_rent = {
    'Tokyo': 2000,
    'Yokohama': 1800,
    'Osaka': 1500,
    'Nagoya': 1300,
    'Sapporo': 1000
}

rent = []
for c, h in zip(city, houseType):
    factor = 1.0
    if h == 'Studio':
        factor = 0.8
    elif h == 'House':
        factor = 1.3
    rent.append(base_rent[c] * factor + np.random.randint(-100, 100))

# 创建 DataFrame
data = pd.DataFrame({
    'city': city,
    'houseType': houseType,
    'rent': rent
})

# 特征编码
encoder = OneHotEncoder(sparse_output=False)
X = encoder.fit_transform(data[['city', 'houseType']])
y = data['rent']

# 训练模型
model = LinearRegression()
model.fit(X, y)

# 测试预测
sample = pd.DataFrame({'city': ['Tokyo'], 'houseType': ['Apartment']})
sample_X = encoder.transform(sample)
predicted_rent = model.predict(sample_X)
print("预测租金（美元/月）：", predicted_rent[0])

# 保存模型和编码器
import joblib
joblib.dump(model, 'rent_model.pkl')
joblib.dump(encoder, 'rent_encoder.pkl')
