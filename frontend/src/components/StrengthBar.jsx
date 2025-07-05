import React from 'react';


function StrengthBar({ score, strength }) {


    const getWidth = () => {

        const scorePercentage = (score / 100) * 100;
        return `${Math.max(0, Math.min(100, scorePercentage))}%`;
    };


    const getColorClass = () => {

        switch (strength) {
            case 'Comprometida':
                return 'strength-bar-compromised';
            case 'Fraca':
                return 'strength-bar-weak';
            case 'Média':
                return 'strength-bar-medium';
            case 'Forte':
                return 'strength-bar-strong';
            case 'Muito Forte':
                return 'strength-bar-very-strong';
            default:
                return ''; 
        }
    };

    const width = getWidth();
    const colorClass = getColorClass();

    return (
        <div className="strength-bar-container">
            <div
                className={`strength-bar-fill ${colorClass}`}
                style={{ width: width }}
            >
            </div>
        </div>
    );
}

export default StrengthBar;