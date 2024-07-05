import React from 'react';
import Chart from 'react-apexcharts';

function PieChart ({ data }) {
  
  const categories = data.map(item => item.category);
  const amounts = data.map(item => item.amount);

  const options = {
    labels: categories
  };

  return (
    <Chart
      options={options}
      series={amounts}
      type="pie"
      width="450"
      height="200"
    />
  );
};

export default PieChart;