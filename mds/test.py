import plotly.graph_objects as go
from sklearn.manifold import MDS

import out

mds_sklearn = MDS(n_components=3, metric=True, dissimilarity='precomputed', normalized_stress=False)
init = out.initial
x_sklearn = mds_sklearn.fit_transform(out.stitches, init=init)
print("Stress:", mds_sklearn.stress_)

marker_data = go.Scatter3d(
    x=x_sklearn[:, 0],
    y=x_sklearn[:, 1],
    z=x_sklearn[:, 2],
    marker=go.scatter3d.Marker(size=3),
    opacity=0.8,
    mode='markers'
)
fig = go.Figure(data=marker_data)
fig.show()
